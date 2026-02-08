package org.openmcptools.common.server.impl.spring;

import org.openmcptools.common.server.toolgroup.ToolProviderImpl;

public class SpringToolNodeProvider extends ToolProviderImpl {

	public SpringToolNodeProvider() {
		super();
		setInputSchemaGenerator(new SpringInputSchemaGenerator());
	}

	public static class Async extends SpringToolNodeProvider {
		public Async() {
			super();
			setOutputSchemaGenerator(new SpringOutputSchemaGenerator.Async());
		}
	}

	public static class Sync extends SpringToolNodeProvider {
		public Sync() {
			super();
			setOutputSchemaGenerator(new SpringOutputSchemaGenerator.Sync());
		}
	}

}
