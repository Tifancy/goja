/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2013-2013 sagyf Yang. The Four Group.
 */

package com.sagyf.jfinal.annotators;

import com.intellij.codeInsight.daemon.RelatedItemLineMarkerInfo;
import com.intellij.codeInsight.daemon.RelatedItemLineMarkerProvider;
import com.intellij.codeInsight.navigation.NavigationGutterIconBuilder;
import com.intellij.icons.AllIcons;
import com.intellij.navigation.GotoRelatedItem;
import com.intellij.openapi.editor.markup.GutterIconRenderer;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleUtilCore;
import com.intellij.openapi.paths.PathReference;
import com.intellij.psi.*;
import com.intellij.psi.xml.XmlFile;
import com.intellij.util.NotNullFunction;
import com.intellij.util.containers.ContainerUtil;
import com.intellij.util.xml.DomElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.*;

/**
 * <p>
 * .
 * </p>
 *
 * @author sagyf yang
 * @version 1.0 2013-12-25 21:47
 * @since JDK 1.6
 */
public class ControllerActionViewAnnotator extends RelatedItemLineMarkerProvider {


    private static final NotNullFunction<PathReference, Collection<? extends PsiElement>> PATH_REFERENCE_CONVERTER =
            new NotNullFunction<PathReference, Collection<? extends PsiElement>>() {
                @NotNull
                @Override
                public Collection<? extends PsiElement> fun(final PathReference pathReference) {
                    final PsiElement resolve = pathReference.resolve();
                    return resolve != null ? Collections.singleton(resolve) : Collections.<PsiElement>emptyList();
                }
            };

    private static final NotNullFunction<PathReference, Collection<? extends GotoRelatedItem>> PATH_REFERENCE_GOTO_RELATED_ITEM_PROVIDER =
            new NotNullFunction<PathReference, Collection<? extends GotoRelatedItem>>() {
                @NotNull
                @Override
                public Collection<? extends GotoRelatedItem> fun(final PathReference pathReference) {
                    final PsiElement resolve = pathReference.resolve();
                    return resolve != null ? Collections.singleton(new GotoRelatedItem(resolve) {
                        @Override
                        public Icon getCustomIcon() {
                            return pathReference.getIcon();
                        }

                        @Override
                        public String getCustomName() {
                            return pathReference.getPath();
                        }
                    }) : Collections.<GotoRelatedItem>emptyList();
                }
            };

    /**
     * Determine the Action-PsiClass.
     *
     * @param psiElement Passed from annotator.
     * @return null if PsiClass cannot be determined or is not suitable.
     */
    @Nullable
    protected abstract PsiClass getActionPsiClass(@NotNull final PsiElement psiElement);

    @Override
    protected void collectNavigationMarkers(final @NotNull PsiElement element,
                                            final Collection<? super RelatedItemLineMarkerInfo> lineMarkerInfos) {
        if (!(element instanceof PsiIdentifier)) return;
        final PsiClass clazz = getActionPsiClass(element.getParent());
        if (clazz == null || clazz.getNameIdentifier() != element) {
            return;
        }

        // do not run on non-public, abstract classes or interfaces
        if (clazz.isInterface() ||
                clazz.isAnnotationType() ||
                !clazz.hasModifierProperty(PsiModifier.PUBLIC) ||
                clazz.hasModifierProperty(PsiModifier.ABSTRACT)) {
            return;
        }

        // short exit if Struts Facet not present
        final Module module = ModuleUtilCore.findModuleForPsiElement(clazz);
        if (module == null ) {
            return;
        }

        final StrutsManager strutsManager = StrutsManager.getInstance(element.getProject());
        final StrutsModel strutsModel = strutsManager.getCombinedModel(module);
        if (strutsModel == null) {
            return;
        }

        installValidationTargets(element, lineMarkerInfos, clazz);

        final List<Action> actions = strutsModel.findActionsByClass(clazz);
        if (actions.isEmpty()) {
            return;
        }

        installActionTargets(element, lineMarkerInfos, actions);
        installActionMethods(lineMarkerInfos, clazz, actions);
    }

    /**
     * Annotate action class to {@code <action>}-declarations.
     *
     * @param element         Class element to annotate.
     * @param lineMarkerInfos Current line markers.
     * @param actions         Corresponding Actions.
     */
    private static void installActionTargets(final PsiElement element,
                                             final Collection<? super RelatedItemLineMarkerInfo> lineMarkerInfos,
                                             final List<Action> actions) {
        final String tooltip = actions.size() == 1 ? StrutsBundle.message("annotators.action.goto.tooltip.single") :
                StrutsBundle.message("annotators.action.goto.tooltip");
        final NavigationGutterIconBuilder<DomElement> gutterIconBuilder =
                NavigationGutterIconBuilder.create(Struts2Icons.Action, NavigationGutterIconBuilder.DEFAULT_DOM_CONVERTOR,
                        NavigationGutterIconBuilder.DOM_GOTO_RELATED_ITEM_PROVIDER)
                        .setAlignment(GutterIconRenderer.Alignment.LEFT)
                        .setPopupTitle(StrutsBundle.message("annotators.action.goto.declaration"))
                        .setTargets(actions)
                        .setTooltipTitle(tooltip)
                        .setCellRenderer(ACTION_RENDERER);
        lineMarkerInfos.add(gutterIconBuilder.createLineMarkerInfo(element));
    }

    /**
     * Annotate action-methods of this class with result(s).
     *
     * @param lineMarkerInfos Current line markers.
     * @param clazz           Class to annotate.
     * @param actions         Corresponding Actions.
     */
    private static void installActionMethods(final Collection<? super RelatedItemLineMarkerInfo> lineMarkerInfos,
                                             final PsiClass clazz,
                                             final List<Action> actions) {
        final Map<PsiMethod, Set<PathReference>> pathReferenceMap = new HashMap<PsiMethod, Set<PathReference>>();
        for (final Action action : actions) {
            final PsiMethod method = action.searchActionMethod();
            if (method == null || !clazz.equals(method.getContainingClass())) {
                continue;
            }

            final Set<PathReference> pathReferences = new HashSet<PathReference>();
            final List<Result> results = action.getResults();
            for (final Result result : results) {
                final PathReference pathReference = result.getValue();
                ContainerUtil.addIfNotNull(pathReferences, pathReference);
            }

            final Set<PathReference> toStore = ContainerUtil.getOrCreate(pathReferenceMap,
                    method,
                    new HashSet<PathReference>());
            toStore.addAll(pathReferences);
            pathReferenceMap.put(method, toStore);
        }

        for (final Map.Entry<PsiMethod, Set<PathReference>> entries : pathReferenceMap.entrySet()) {
            final NavigationGutterIconBuilder<PathReference> gutterIconBuilder =
                    NavigationGutterIconBuilder.create(AllIcons.Hierarchy.Base, PATH_REFERENCE_CONVERTER,
                            PATH_REFERENCE_GOTO_RELATED_ITEM_PROVIDER)
                            .setAlignment(GutterIconRenderer.Alignment.LEFT)
                            .setPopupTitle(StrutsBundle.message("annotators.action.goto.result"))
                            .setTargets(entries.getValue())
                            .setTooltipTitle(StrutsBundle.message("annotators.action.goto.result.tooltip"));

            lineMarkerInfos.add(gutterIconBuilder.createLineMarkerInfo(entries.getKey()));
        }
    }

    /**
     * Related {@code validation.xml} files.
     *
     * @param element         Class element to annotate.
     * @param lineMarkerInfos Current line markers.
     * @param clazz           Class to find validation files for.
     */
    private static void installValidationTargets(final PsiElement element,
                                                 final Collection<? super RelatedItemLineMarkerInfo> lineMarkerInfos,
                                                 final PsiClass clazz) {
        final List<XmlFile> files = ValidatorManager.getInstance(element.getProject()).findValidationFilesFor(clazz);
        if (files.isEmpty()) {
            return;
        }

        final NavigationGutterIconBuilder<PsiElement> validatorBuilder =
                NavigationGutterIconBuilder.create(StrutsIcons.VALIDATION_CONFIG_FILE)
                        .setAlignment(GutterIconRenderer.Alignment.LEFT)
                        .setTargets(files)
                        .setPopupTitle(StrutsBundle.message("annotators.action.goto.validation"))
                        .setTooltipTitle(StrutsBundle.message("annotators.action.goto.validation.tooltip"));
        lineMarkerInfos.add(validatorBuilder.createLineMarkerInfo(element));
    }

}
