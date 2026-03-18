import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.openmcptools.common.model.AbstractBase;
import org.openmcptools.common.model.Annotations;
import org.openmcptools.common.model.Converter;
import org.openmcptools.common.model.Group;
import org.openmcptools.common.model.Icon;
import org.openmcptools.common.model.Prompt;
import org.openmcptools.common.model.PromptArgument;
import org.openmcptools.common.model.Resource;
import org.openmcptools.common.model.Role;
import org.openmcptools.common.model.Tool;
import org.openmcptools.common.model.ToolAnnotations;

/**
 * Translation of the Common.ts test suite to Java JUnit 5.
 */
public class CommonTest {

	// =========================================================================
	// AbstractBase
	// =========================================================================

	@Nested
	@DisplayName("AbstractBase")
	class AbstractBaseTests {

		@Test
		@DisplayName("throws when name is empty")
		void throwsWhenNameIsEmpty() {
			Exception exception = assertThrows(IllegalArgumentException.class, () -> new Group(""));
			assertEquals("name must not be null, empty, or blank", exception.getMessage());
		}

		@Test
		@DisplayName("uses DEFAULT_SEPARATOR by default")
		void usesDefaultSeparatorByDefault() {
			Group g = new Group("root");
			assertEquals(AbstractBase.DEFAULT_SEPARATOR, g.getNameSeparator());
		}

		@Test
		@DisplayName("allows a custom separator")
		void allowsACustomSeparator() {
			Group g = new Group("root", "/");
			assertEquals("/", g.getNameSeparator());
		}
	}

	// =========================================================================
	// Group — hierarchy & fully qualified names
	// =========================================================================

	@Nested
	@DisplayName("Group")
	class GroupTests {

		@Test
		@DisplayName("is root when created standalone")
		void isRootWhenCreatedStandalone() {
			Group root = new Group("root");
			assertTrue(root.isRoot());
			assertNull(root.getParent());
			assertEquals(root, root.getRoot());
		}

		@Test
		@DisplayName("builds a parent → child hierarchy")
		void buildsAParentChildHierarchy() {
			Group root = new Group("com");
			Group mid = new Group("example");
			Group leaf = new Group("api");

			assertTrue(root.addChildGroup(mid));
			assertTrue(mid.addChildGroup(leaf));

			assertEquals(root, mid.getParent());
			assertEquals(mid, leaf.getParent());
			assertFalse(leaf.isRoot());
			assertEquals(root, leaf.getRoot());
		}

		@Test
		@DisplayName("computes fully qualified name through the chain")
		void computesFullyQualifiedNameThroughTheChain() {
			Group root = new Group("com");
			Group mid = new Group("example");
			Group leaf = new Group("api");

			root.addChildGroup(mid);
			mid.addChildGroup(leaf);

			assertEquals("com", root.getFullyQualifiedName());
			assertEquals("com.example", mid.getFullyQualifiedName());
			assertEquals("com.example.api", leaf.getFullyQualifiedName());
		}

		@Test
		@DisplayName("uses custom separator in fully qualified name")
		void useCustomSeparatorInFullyQualifiedName() {
			Group root = new Group("com", "/");
			Group child = new Group("api", "/");
			root.addChildGroup(child);

			assertEquals("com/api", child.getFullyQualifiedName());
		}

		@Test
		@DisplayName("prevents duplicate child groups")
		void preventsDuplicateChildGroups() {
			Group root = new Group("root");
			Group child = new Group("child");

			assertTrue(root.addChildGroup(child));
			assertFalse(root.addChildGroup(child));
			assertEquals(1, root.getChildrenGroups().size());
		}

		@Test
		@DisplayName("removes child group and clears parent")
		void removesChildGroupAndClearsParent() {
			Group root = new Group("root");
			Group child = new Group("child");

			root.addChildGroup(child);
			assertTrue(root.removeChildGroup(child));
			assertEquals(0, root.getChildrenGroups().size());
			assertNull(child.getParent());
		}

		@Test
		@DisplayName("returns false when removing non-existent child group")
		void returnsFalseWhenRemovingNonExistentChildGroup() {
			Group root = new Group("root");
			Group other = new Group("other");
			assertFalse(root.removeChildGroup(other));
		}

		@Test
		@DisplayName("stores optional properties (title, description, meta, icons)")
		void storesOptionalProperties() {
			Group g = new Group("g");
			g.setTitle("My Group");
			g.setDescription("A description");
			Map<String, Object> meta = new HashMap<>();
			meta.put("key", "value");
			g.setMeta(meta);

			Icon icon = new Icon();
			icon.setSrc("icon.png");
			icon.setMimeType("image/png");
			g.setIcons(Collections.singletonList(icon));

			assertEquals("My Group", g.getTitle());
			assertEquals("A description", g.getDescription());
			assertEquals(meta, g.getMeta());
			assertEquals(1, g.getIcons().size());
			assertEquals("icon.png", g.getIcons().get(0).getSrc());
		}
	}

	// =========================================================================
	// Group ↔ Tool bidirectional relationship
	// =========================================================================

	@Nested
	@DisplayName("Group ↔ Tool")
	class GroupToolRelationshipTests {

		@Test
		@DisplayName("adds a tool and links parent group bidirectionally")
		void addsAToolAndLinksParentGroupBidirectionally() {
			Group group = new Group("g");
			Tool tool = new Tool("t");

			assertTrue(group.addChildTool(tool));
			assertTrue(group.getChildrenTools().contains(tool));
			assertTrue(tool.getParentGroups().contains(group));
		}

		@Test
		@DisplayName("prevents duplicate tool additions")
		void preventsDuplicateToolAdditions() {
			Group group = new Group("g");
			Tool tool = new Tool("t");

			group.addChildTool(tool);
			assertFalse(group.addChildTool(tool));
			assertEquals(1, group.getChildrenTools().size());
		}

		@Test
		@DisplayName("removes a tool and unlinks parent group")
		void removesAToolAndUnlinksParentGroup() {
			Group group = new Group("g");
			Tool tool = new Tool("t");

			group.addChildTool(tool);
			assertTrue(group.removeChildTool(tool));
			assertEquals(0, group.getChildrenTools().size());
			assertEquals(0, tool.getParentGroups().size());
		}

		@Test
		@DisplayName("returns false when removing non-existent tool")
		void returnsFalseWhenRemovingNonExistentTool() {
			Group group = new Group("g");
			Tool tool = new Tool("t");
			assertFalse(group.removeChildTool(tool));
		}
	}

	// =========================================================================
	// Group ↔ Prompt bidirectional relationship
	// =========================================================================

	@Nested
	@DisplayName("Group ↔ Prompt")
	class GroupPromptRelationshipTests {

		@Test
		@DisplayName("adds a prompt and links parent group bidirectionally")
		void addsAPromptAndLinksParentGroupBidirectionally() {
			Group group = new Group("g");
			Prompt prompt = new Prompt("p");

			assertTrue(group.addChildPrompt(prompt));
			assertTrue(group.getChildPrompts().contains(prompt));
			assertTrue(prompt.getParentGroups().contains(group));
		}

		@Test
		@DisplayName("prevents duplicate prompt additions")
		void preventsDuplicatePromptAdditions() {
			Group group = new Group("g");
			Prompt prompt = new Prompt("p");

			group.addChildPrompt(prompt);
			assertFalse(group.addChildPrompt(prompt));
		}

		@Test
		@DisplayName("removes a prompt and unlinks parent group")
		void removesAPromptAndUnlinksParentGroup() {
			Group group = new Group("g");
			Prompt prompt = new Prompt("p");

			group.addChildPrompt(prompt);
			assertTrue(group.removeChildPrompt(prompt));
			assertEquals(0, group.getChildPrompts().size());
			assertEquals(0, prompt.getParentGroups().size());
		}
	}

	// =========================================================================
	// Group ↔ Resource bidirectional relationship
	// =========================================================================

	@Nested
	@DisplayName("Group ↔ Resource")
	class GroupResourceRelationshipTests {

		@Test
		@DisplayName("adds a resource and links parent group bidirectionally")
		void addsAResourceAndLinksParentGroupBidirectionally() {
			Group group = new Group("g");
			Resource resource = new Resource("r", "file:///");

			assertTrue(group.addChildResource(resource));
			assertTrue(group.getChildResources().contains(resource));
			assertTrue(resource.getParentGroups().contains(group));
		}

		@Test
		@DisplayName("prevents duplicate resource additions")
		void preventsDuplicateResourceAdditions() {
			Group group = new Group("g");
			Resource resource = new Resource("r", "file:///");

			group.addChildResource(resource);
			assertFalse(group.addChildResource(resource));
		}

		@Test
		@DisplayName("removes a resource and unlinks parent group")
		void removesAResourceAndUnlinksParentGroup() {
			Group group = new Group("g");
			Resource resource = new Resource("r", "file:///");

			group.addChildResource(resource);
			assertTrue(group.removeChildResource(resource));
			assertEquals(0, group.getChildResources().size());
			assertEquals(0, resource.getParentGroups().size());
		}
	}

	// =========================================================================
	// AbstractLeaf — shared leaf behavior
	// =========================================================================

	@Nested
	@DisplayName("AbstractLeaf (via Tool)")
	class AbstractLeafTests {

		@Test
		@DisplayName("returns its name as fully qualified name")
		void returnsItsNameAsFullyQualifiedName() {
			Tool tool = new Tool("myTool");
			assertEquals("myTool", tool.getFullyQualifiedName());
		}

		@Test
		@DisplayName("can belong to multiple parent groups")
		void canBelongToMultipleParentGroups() {
			Group g1 = new Group("g1");
			Group g2 = new Group("g2");
			Tool tool = new Tool("shared");

			g1.addChildTool(tool);
			g2.addChildTool(tool);

			assertEquals(2, tool.getParentGroups().size());
			assertTrue(tool.getParentGroups().contains(g1));
			assertTrue(tool.getParentGroups().contains(g2));
		}

		@Test
		@DisplayName("getParentGroupRoots returns roots of all parent groups")
		void getParentGroupRootsReturnsRootsOfAllParentGroups() {
			Group root = new Group("root");
			Group child = new Group("child");
			root.addChildGroup(child);

			Tool tool = new Tool("tool");
			child.addChildTool(tool);

			List<Group> roots = tool.getParentGroupRoots();
			assertEquals(1, roots.size());
			assertEquals(root, roots.get(0));
		}

		@Test
		@DisplayName("prevents duplicate parent group registration")
		void preventsDuplicateParentGroupRegistration() {
			Group group = new Group("g");
			Tool tool = new Tool("t");

			assertTrue(tool.addParentGroup(group));
			assertFalse(tool.addParentGroup(group));
			assertEquals(1, tool.getParentGroups().size());
		}

		@Test
		@DisplayName("returns false when removing non-existent parent group")
		void returnsFalseWhenRemovingNonExistentParentGroup() {
			Tool tool = new Tool("t");
			Group group = new Group("g");
			assertFalse(tool.removeParentGroup(group));
		}
	}

	// =========================================================================
	// Tool
	// =========================================================================

	@Nested
	@DisplayName("Tool")
	class ToolSpecificTests {

		@Test
		@DisplayName("stores optional schemas and annotations")
		void storesOptionalSchemasAndAnnotations() {
			Tool tool = new Tool("myTool");
			tool.setInputSchema("{ \"type\": \"object\" }");
			tool.setOutputSchema("{ \"type\": \"string\" }");

			ToolAnnotations ann = new ToolAnnotations();
			ann.setTitle("MyTool");
			ann.setReadOnlyHint(true);
			ann.setDestructiveHint(false);
			ann.setIdempotentHint(true);
			ann.setOpenWorldHint(false);
			ann.setReturnDirect(true);
			tool.setToolAnnotations(ann);

			assertEquals("{ \"type\": \"object\" }", tool.getInputSchema());
			assertEquals("{ \"type\": \"string\" }", tool.getOutputSchema());
			assertTrue(tool.getToolAnnotations().getReadOnlyHint());
			assertTrue(tool.getToolAnnotations().getReturnDirect());
		}
	}

	// =========================================================================
	// Prompt & PromptArgument
	// =========================================================================

	@Nested
	@DisplayName("Prompt")
	class PromptTests {

		@Test
		@DisplayName("adds and removes prompt arguments")
		void addsAndRemovesPromptArguments() {
			Prompt prompt = new Prompt("myPrompt");
			PromptArgument arg = new PromptArgument("query");
			arg.setRequired(true);

			assertTrue(prompt.addPromptArgument(arg));
			assertEquals(1, prompt.getPromptArguments().size());
			assertEquals("query", prompt.getPromptArguments().get(0).getName());
			assertTrue(prompt.getPromptArguments().get(0).isRequired());

			assertTrue(prompt.removePromptArgument(arg));
			assertEquals(0, prompt.getPromptArguments().size());
		}

		@Test
		@DisplayName("prevents duplicate prompt arguments")
		void preventsDuplicatePromptArguments() {
			Prompt prompt = new Prompt("myPrompt");
			PromptArgument arg = new PromptArgument("query");

			prompt.addPromptArgument(arg);
			assertFalse(prompt.addPromptArgument(arg));
			assertEquals(1, prompt.getPromptArguments().size());
		}

		@Test
		@DisplayName("returns false when removing non-existent argument")
		void returnsFalseWhenRemovingNonExistentArgument() {
			Prompt prompt = new Prompt("myPrompt");
			PromptArgument arg = new PromptArgument("other");
			assertFalse(prompt.removePromptArgument(arg));
		}
	}

	// =========================================================================
	// Resource
	// =========================================================================

	@Nested
	@DisplayName("Resource")
	class ResourceTests {

		@Test
		@DisplayName("stores optional URI, size, mimeType and annotations")
		void storesOptionalProperties() {
			Resource resource = new Resource("doc", "file:///data.json");
			resource.setSize(1024L);
			resource.setMimeType("application/json");

			Annotations ann = new Annotations(Collections.singletonList(Role.USER), 1.0d);
			resource.setAnnotations(ann);

			assertEquals("file:///data.json", resource.getUri());
			assertEquals(1024L, resource.getSize());
			assertEquals("application/json", resource.getMimeType());
			assertEquals(Collections.singletonList(Role.USER), resource.getAnnotations().getAudience());
			assertEquals(1, resource.getAnnotations().getPriority());
		}
	}

	// =========================================================================
	// Converter interface (structural typing check)
	// =========================================================================

	@Nested
	@DisplayName("Converter interface")
	class ConverterInterfaceTests {

		static class ToolExt {
			String n;

			ToolExt(String n) {
				this.n = n;
			}

			@Override
			public boolean equals(Object o) {
				if (this == o)
					return true;
				if (o == null || getClass() != o.getClass())
					return false;
				ToolExt toolExt = (ToolExt) o;
				return Objects.equals(n, toolExt.n);
			}
		}

		@Test
		@DisplayName("can be implemented and used for bidirectional conversion")
		void canBeImplementedAndUsed() {
			Converter<Tool, ToolExt> toolConverter = new Converter<Tool, ToolExt>() {
				@Override
				public ToolExt convertFrom(Tool tool) {
					return new ToolExt(tool.getName());
				}

				@Override
				public Tool convertTo(ToolExt ext) {
					return new Tool(ext.n);
				}

			};

			Tool tool = new Tool("test");
			ToolExt ext = toolConverter.convertFrom(tool);
			assertEquals(new ToolExt("test"), ext);

			Tool back = toolConverter.convertTo(ext);
			assertEquals("test", back.getName());
		}
	}

	// =========================================================================
	// Complex tree scenario
	// =========================================================================

	@Nested
	@DisplayName("Complex tree scenario")
	class ComplexTreeTests {

		@Test
		@DisplayName("builds a full tree and verifies all relationships")
		void buildsAFullTreeAndVerifiesAllRelationships() {
			// com.example.api
			Group com = new Group("com");
			Group example = new Group("example");
			Group api = new Group("api");

			com.addChildGroup(example);
			example.addChildGroup(api);

			// Tools under api
			Tool listTool = new Tool("list");
			Tool createTool = new Tool("create");
			api.addChildTool(listTool);
			api.addChildTool(createTool);

			// Prompts under example
			Prompt helpPrompt = new Prompt("help");
			example.addChildPrompt(helpPrompt);

			// Resources under com
			Resource readme = new Resource("readme", "file:///README.md");
			com.addChildResource(readme);

			// Verify structure
			assertEquals(1, com.getChildrenGroups().size());
			assertEquals(1, example.getChildrenGroups().size());
			assertEquals(2, api.getChildrenTools().size());
			assertEquals(1, example.getChildPrompts().size());
			assertEquals(1, com.getChildResources().size());

			// Verify FQN
			assertEquals("com.example.api", api.getFullyQualifiedName());

			// Verify roots from leaves
			assertEquals(com, listTool.getParentGroupRoots().get(0));
			assertEquals(com, helpPrompt.getParentGroupRoots().get(0));
			assertEquals(com, readme.getParentGroupRoots().get(0));

			// Remove tool and check cleanup
			api.removeChildTool(listTool);
			assertEquals(1, api.getChildrenTools().size());
			assertEquals(0, listTool.getParentGroups().size());
		}
	}

	// =========================================================================
	// FAILURE / EDGE CASE / STATE CONSISTENCY TESTS
	// =========================================================================

	@Nested
	@DisplayName("AbstractBase — failure cases")
	class AbstractBaseFailureTests {

		@Test
		@DisplayName("throws when name is null")
		void throwsWhenNameIsNull() {
			assertThrows(IllegalArgumentException.class, () -> new Group(null));
		}

		@Test
		@DisplayName("name remains the value set at construction")
		void nameRemainsTheValueSetAtConstruction() {
			Group g = new Group("immutable");
			assertEquals("immutable", g.getName());
		}
	}

	@Nested
	@DisplayName("Group — double remove and state consistency")
	class GroupConsistencyTests {

		@Test
		@DisplayName("double-removing a child group is idempotent")
		void doubleRemovingAChildGroupIsIdempotent() {
			Group root = new Group("root");
			Group child = new Group("child");

			root.addChildGroup(child);
			assertTrue(root.removeChildGroup(child));
			assertFalse(root.removeChildGroup(child));
			assertEquals(0, root.getChildrenGroups().size());
			assertNull(child.getParent());
		}

		@Test
		@DisplayName("double-removing a child tool is idempotent")
		void doubleRemovingAChildToolIsIdempotent() {
			Group group = new Group("g");
			Tool tool = new Tool("t");

			group.addChildTool(tool);
			group.removeChildTool(tool);
			assertFalse(group.removeChildTool(tool));
			assertEquals(0, group.getChildrenTools().size());
			assertEquals(0, tool.getParentGroups().size());
		}

		@Test
		@DisplayName("double-removing a child prompt is idempotent")
		void doubleRemovingAChildPromptIsIdempotent() {
			Group group = new Group("g");
			Prompt prompt = new Prompt("p");

			group.addChildPrompt(prompt);
			group.removeChildPrompt(prompt);
			assertFalse(group.removeChildPrompt(prompt));
		}

		@Test
		@DisplayName("double-removing a child resource is idempotent")
		void doubleRemovingAChildResourceIsIdempotent() {
			Group group = new Group("g");
			Resource resource = new Resource("r", "file:///README.md");

			group.addChildResource(resource);
			group.removeChildResource(resource);
			assertFalse(group.removeChildResource(resource));
		}

		@Test
		@DisplayName("re-adding a child group after removal works correctly")
		void reAddingAChildGroupAfterRemovalWorksCorrectly() {
			Group root = new Group("root");
			Group child = new Group("child");

			root.addChildGroup(child);
			root.removeChildGroup(child);
			assertNull(child.getParent());

			assertTrue(root.addChildGroup(child));
			assertEquals(root, child.getParent());
			assertEquals(1, root.getChildrenGroups().size());
		}

		@Test
		@DisplayName("re-adding a tool after removal restores bidirectional link")
		void reAddingAToolAfterRemovalRestoresBidirectionalLink() {
			Group group = new Group("g");
			Tool tool = new Tool("t");

			group.addChildTool(tool);
			group.removeChildTool(tool);

			assertTrue(group.addChildTool(tool));
			assertTrue(group.getChildrenTools().contains(tool));
			assertTrue(tool.getParentGroups().contains(group));
		}

		@Test
		@DisplayName("removing child group from wrong parent returns false")
		void removingChildGroupFromWrongParent() {
			Group parent1 = new Group("p1");
			Group parent2 = new Group("p2");
			Group child = new Group("child");

			parent1.addChildGroup(child);

			assertFalse(parent2.removeChildGroup(child));

			assertEquals(parent1, child.getParent());
			assertTrue(parent1.getChildrenGroups().contains(child));
		}
	}

	@Nested
	@DisplayName("Group — deeply nested tree")
	class DeepTreeTests {

		@Test
		@DisplayName("getRoot traverses 5 levels deep")
		void getRootTraverses5LevelsDeep() {
			Group g1 = new Group("l1");
			Group g2 = new Group("l2");
			Group g3 = new Group("l3");
			Group g4 = new Group("l4");
			Group g5 = new Group("l5");

			g1.addChildGroup(g2);
			g2.addChildGroup(g3);
			g3.addChildGroup(g4);
			g4.addChildGroup(g5);

			assertEquals(g1, g5.getRoot());
			assertEquals("l1.l2.l3.l4.l5", g5.getFullyQualifiedName());
		}

		@Test
		@DisplayName("FQN updates correctly after re-parenting a subtree")
		void fqnUpdatesCorrectlyAfterReparenting() {
			Group root1 = new Group("com");
			Group root2 = new Group("org");
			Group child = new Group("api");

			root1.addChildGroup(child);
			assertEquals("com.api", child.getFullyQualifiedName());

			root1.removeChildGroup(child);
			root2.addChildGroup(child);
			assertEquals("org.api", child.getFullyQualifiedName());
			assertEquals(root2, child.getRoot());
		}
	}

	@Nested
	@DisplayName("AbstractLeaf — failure cases")
	class AbstractLeafFailureTests {

		@Test
		@DisplayName("removing parent group from tool not in that group returns false")
		void removingParentGroupFromToolNotInThatGroup() {
			Group g1 = new Group("g1");
			Group g2 = new Group("g2");
			Tool tool = new Tool("t");

			g1.addChildTool(tool);

			assertFalse(tool.removeParentGroup(g2));
			assertEquals(1, tool.getParentGroups().size());
			assertTrue(tool.getParentGroups().contains(g1));
		}

		@Test
		@DisplayName("getParentGroupRoots with multiple disjoint trees")
		void getParentGroupRootsWithMultipleDisjointTrees() {
			Group rootA = new Group("rootA");
			Group childA = new Group("childA");
			rootA.addChildGroup(childA);

			Group rootB = new Group("rootB");

			Tool tool = new Tool("shared");
			childA.addChildTool(tool);
			rootB.addChildTool(tool);

			List<Group> roots = tool.getParentGroupRoots();
			assertEquals(2, roots.size());
			assertTrue(roots.contains(rootA));
			assertTrue(roots.contains(rootB));
		}
	}

	@Nested
	@DisplayName("Prompt — failure cases")
	class PromptFailureTests {

		@Test
		@DisplayName("double-removing a prompt argument is idempotent")
		void doubleRemovingAPromptArgumentIsIdempotent() {
			Prompt prompt = new Prompt("p");
			PromptArgument arg = new PromptArgument("x");

			prompt.addPromptArgument(arg);
			prompt.removePromptArgument(arg);
			assertFalse(prompt.removePromptArgument(arg));
			assertEquals(0, prompt.getPromptArguments().size());
		}

		@Test
		@DisplayName("re-adding a prompt argument after removal works")
		void reAddingAPromptArgumentAfterRemovalWorks() {
			Prompt prompt = new Prompt("p");
			PromptArgument arg = new PromptArgument("x");
			arg.setRequired(true);

			prompt.addPromptArgument(arg);
			prompt.removePromptArgument(arg);
			assertTrue(prompt.addPromptArgument(arg));
			assertEquals(1, prompt.getPromptArguments().size());
		}
	}

	@Nested
	@DisplayName("Optional properties — null by default")
	class DefaultPropertyTests {

		@Test
		@DisplayName("Group optional properties are null when not set")
		void groupOptionalPropertiesAreNull() {
			Group g = new Group("g");
			assertNull(g.getTitle());
			assertNull(g.getDescription());
			assertNull(g.getMeta());
			assertNull(g.getIcons());
		}

		@Test
		@DisplayName("Tool optional properties are null when not set")
		void toolOptionalPropertiesAreNull() {
			Tool t = new Tool("t");
			assertNull(t.getInputSchema());
			assertNull(t.getOutputSchema());
			assertNull(t.getToolAnnotations());
		}

		@Test
		@DisplayName("Resource optional properties are null when not set")
		void resourceOptionalPropertiesAreNull() {
			Resource r = new Resource("r", "file://foo/bar");
			assertNotNull(r.getUri());
			assertNull(r.getSize());
			assertNull(r.getMimeType());
			assertNull(r.getAnnotations());
		}
	}

}
