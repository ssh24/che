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
package org.eclipse.che.workspace.infrastructure.docker.environment.dockerimage;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.google.common.collect.ImmutableMap;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.eclipse.che.api.core.ValidationException;
import org.eclipse.che.api.workspace.server.spi.environment.InternalMachineConfig;
import org.eclipse.che.api.workspace.server.spi.environment.InternalRecipe;
import org.eclipse.che.api.workspace.server.spi.environment.MachineConfigsValidator;
import org.eclipse.che.api.workspace.server.spi.environment.MemoryAttributeProvisioner;
import org.eclipse.che.api.workspace.server.spi.environment.RecipeRetriever;
import org.mockito.Mock;
import org.mockito.testng.MockitoTestNGListener;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

/** @author Alexander Garagatyi */
@Listeners(MockitoTestNGListener.class)
public class DockerImageEnvironmentFactoryTest {
  private static final String MACHINE_NAME = "machine";

  @Mock private InternalRecipe recipe;
  @Mock private RecipeRetriever recipeRetriever;
  @Mock private MachineConfigsValidator machinesValidator;
  @Mock private MemoryAttributeProvisioner memoryProvisioner;

  private DockerImageEnvironmentFactory factory;

  @BeforeMethod
  public void setUp() throws Exception {
    factory =
        new DockerImageEnvironmentFactory(recipeRetriever, machinesValidator, memoryProvisioner);

    when(recipe.getType()).thenReturn(DockerImageEnvironment.TYPE);
    when(recipe.getContent()).thenReturn("");
  }

  @Test
  public void testRamProvisionerIsInvoked() throws Exception {
    final Map<String, InternalMachineConfig> machines =
        ImmutableMap.of(MACHINE_NAME, mock(InternalMachineConfig.class));

    factory.doCreate(recipe, machines, Collections.emptyList());

    verify(memoryProvisioner).provision(any(), eq(0L), eq(0L));
  }

  @Test
  public void testInsertsEmptyMachineConfigIfNoMachines() throws Exception {
    DockerImageEnvironment env = factory.doCreate(recipe, new HashMap<>(), Collections.emptyList());
    Assert.assertEquals(1, env.getMachines().size());
  }

  @Test(expectedExceptions = ValidationException.class)
  public void shouldFailIfMoreThanOneMachineConfigProvided() throws Exception {
    Map<String, InternalMachineConfig> machines =
        ImmutableMap.of("one", new InternalMachineConfig(), "two", new InternalMachineConfig());

    factory.doCreate(recipe, machines, Collections.emptyList());
  }
}
