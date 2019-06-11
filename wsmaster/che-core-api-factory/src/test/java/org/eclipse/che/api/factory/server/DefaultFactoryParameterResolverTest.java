/*
 * Copyright (c) 2012-2018 Red Hat, Inc.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *   Red Hat, Inc. - initial API and implementation
 */
package org.eclipse.che.api.factory.server;

import org.eclipse.che.api.workspace.server.devfile.URLFetcher;
import org.eclipse.che.api.workspace.server.wsplugins.PluginFQNParser;
import org.mockito.Mock;
import org.mockito.testng.MockitoTestNGListener;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

@Listeners(value = {MockitoTestNGListener.class})
public class DefaultFactoryParameterResolverTest {

  private static final String DEVFILE =
      ""
          + "specVersion: 0.0.1\n"
          + "name: test\n"
          + "components:\n"
          + "- type: kubernetes\n"
          + "  alias: component\n"
          + "  reference: ../localfile\n";

  @Mock private URLFetcher urlFetcher;
  private PluginFQNParser fqnParser = new PluginFQNParser();

  @Test
  public void shouldResolveRelativeFiles() throws Exception {
    //    // given
    //
    //    // we need to set up an "almost real" devfile converter which is a little bit involved
    //    DevfileSchemaValidator validator = new DevfileSchemaValidator(new
    // DevfileSchemaProvider());
    //
    //    Map<String, ComponentIntegrityValidator> validators = new HashMap<>();
    //    validators.put(EDITOR_COMPONENT_TYPE, new NoopComponentIntegrityValidator());
    //    validators.put(PLUGIN_COMPONENT_TYPE, new NoopComponentIntegrityValidator());
    //    validators.put(KUBERNETES_COMPONENT_TYPE, new NoopComponentIntegrityValidator());
    //    validators.put(OPENSHIFT_COMPONENT_TYPE, new NoopComponentIntegrityValidator());
    //
    //    DevfileIntegrityValidator integrityValidator = new DevfileIntegrityValidator(validators);
    //    Set<ComponentProvisioner> componentProvisioners = new HashSet<>();
    //    Map<String, ComponentToWorkspaceApplier> appliers = new HashMap<>();
    //    ComponentToWorkspaceApplier applier = mock(ComponentToWorkspaceApplier.class);
    //    appliers.put("kubernetes", applier);
    //
    //    doAnswer(
    //            i -> {
    //              // in here we mock that the component applier requests the contents of the
    // referenced
    //              // local file. That's all we need to happen
    //              FileContentProvider p = i.getArgument(2);
    //              ComponentImpl component = i.getArgument(1);
    //              p.fetchContent(component.getReference());
    //              return null;
    //            })
    //        .when(applier)
    //        .apply(any(), any(), any());
    //
    //    DevfileConverter devfileConverter =
    //        new DevfileConverter(
    //            new ProjectConverter(),
    //            new CommandConverter(),
    //            componentProvisioners,
    //            appliers,
    //            new DefaultEditorProvisioner(null, new String[] {}, fqnParser),
    //            new URLFetcher());
    //
    //    WorkspaceManager workspaceManager = mock(WorkspaceManager.class);
    //
    //    DevfileManager devfileManager =
    //        new DevfileManager(validator, integrityValidator, devfileConverter, workspaceManager);
    //
    //    URLFactoryBuilder factoryBuilder =
    //        new URLFactoryBuilder("editor", "plugin", urlFetcher, devfileManager);
    //
    //    DefaultFactoryParameterResolver res =
    //        new DefaultFactoryParameterResolver(factoryBuilder, urlFetcher);
    //
    //    // set up our factory with the location of our devfile that is referencing our localfile
    //    Map<String, String> factoryParameters = new HashMap<>();
    //    factoryParameters.put(URL_PARAMETER_NAME, "scheme:/myloc/devfile");
    //    doReturn(DEVFILE).when(urlFetcher).fetchSafely(eq("scheme:/myloc/devfile"));
    //    doReturn("localfile").when(urlFetcher).fetch("scheme:/localfile");
    //
    //    // when
    //    res.createFactory(factoryParameters);

    // then
    // TODO must fix this test
    // verify(urlFetcher).fetch(eq("scheme:/localfile"));
  }
}
