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
package org.eclipse.che.workspace.infrastructure.kubernetes.devfile;

import org.eclipse.che.api.core.model.workspace.devfile.Component;
import org.eclipse.che.api.workspace.server.devfile.FileContentProvider;
import org.eclipse.che.api.workspace.server.devfile.convert.component.ComponentResolver;
import org.eclipse.che.api.workspace.server.model.impl.devfile.DevfileImpl;

public class KubernetesComponentResolver implements ComponentResolver {

  @Override
  public void resolve(
      Component component, DevfileImpl devfile, FileContentProvider contentProvider) {}

  //  private String retrieveContent(
  //      Component recipeComponent, @Nullable FileContentProvider fileContentProvider)
  //      throws DevfileException {
  //    checkArgument(fileContentProvider != null, "Content provider must not be null");
  //    if (!isNullOrEmpty(recipeComponent.getReferenceContent())) {
  //      return recipeComponent.getReferenceContent();
  //    }
  //
  //    String recipeFileContent;
  //    try {
  //      recipeFileContent = fileContentProvider.fetchContent(recipeComponent.getReference());
  //    } catch (DevfileException e) {
  //      throw new DevfileException(
  //          format(
  //              "Fetching content of file `%s` specified in `reference` field of component `%s` is
  // not supported. "
  //                  + "Please provide its content in `referenceContent` field. Cause: %s",
  //              recipeComponent.getReference(),
  //              getIdentifiableComponentName(recipeComponent),
  //              e.getMessage()),
  //          e);
  //    } catch (IOException e) {
  //      throw new DevfileException(
  //          format(
  //              "Error during recipe content retrieval for component '%s' with type '%s': %s",
  //              getIdentifiableComponentName(recipeComponent),
  //              recipeComponent.getType(),
  //              e.getMessage()),
  //          e);
  //    }
  //    if (isNullOrEmpty(recipeFileContent)) {
  //      throw new DevfileException(
  //          format(
  //              "The reference file '%s' defined in component '%s' is empty.",
  //              recipeComponent.getReference(), getIdentifiableComponentName(recipeComponent)));
  //    }
  //    return recipeFileContent;
  //  }

}
