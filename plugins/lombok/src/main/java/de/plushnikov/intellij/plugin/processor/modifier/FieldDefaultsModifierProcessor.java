package de.plushnikov.intellij.plugin.processor.modifier;

import com.google.common.collect.Iterables;
import com.intellij.psi.PsiAnnotation;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiField;
import com.intellij.psi.PsiModifier;
import com.intellij.psi.PsiModifierList;
import com.intellij.psi.util.PsiTreeUtil;

import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Set;

import de.plushnikov.intellij.plugin.util.PsiAnnotationSearchUtil;
import de.plushnikov.intellij.plugin.util.PsiAnnotationUtil;
import lombok.AccessLevel;

/**
 * Processor for <strong>experimental</strong> {@literal @FieldDefaults} feature of Lombok.
 *
 * @author Alexej Kubarev
 * @see <a href="https://projectlombok.org/features/experimental/FieldDefaults.html">Lombok Feature: Field Defaults</a>
 */
public class FieldDefaultsModifierProcessor implements ModifierProcessor {

  @Override
  public boolean isSupported(@NotNull PsiModifierList modifierList) {

    // FieldDefaults only change modifiers of class fields
    if (!(modifierList.getParent() instanceof PsiField)) {
      return false;
    }

    PsiClass searchableClass = PsiTreeUtil.getParentOfType(modifierList, PsiClass.class, true);

    return null != searchableClass && PsiAnnotationSearchUtil.isAnnotatedWith(searchableClass, lombok.experimental.FieldDefaults.class);
  }

  @Override
  @NotNull
  public Set<String> transformModifiers(@NotNull PsiModifierList modifierList, @NotNull final Set<String> modifiers) {

    PsiClass searchableClass = PsiTreeUtil.getParentOfType(modifierList, PsiClass.class, true);
    if (searchableClass == null) {
      return modifiers; // Should not get here, but safer to check
    }

    PsiAnnotation fieldDefaultsAnnotation = PsiAnnotationSearchUtil.findAnnotation(searchableClass, lombok.experimental.FieldDefaults.class);
    if (fieldDefaultsAnnotation == null) {
      return modifiers; // Should not get here, but safer to check
    }

    PsiField parentElement = (PsiField) modifierList.getParent();

    // FINAL
    // Is @FieldDefaults(makeFinal = true)?
    if ((PsiAnnotationUtil.getBooleanAnnotationValue(fieldDefaultsAnnotation, "makeFinal", false)) && (!PsiAnnotationSearchUtil.isAnnotatedWith(parentElement, lombok.experimental.NonFinal.class))) {
      modifiers.add(PsiModifier.FINAL);
    }

    // VISIBILITY
    Collection<String> defaultLevels = PsiAnnotationUtil.getAnnotationValues(fieldDefaultsAnnotation, "level", String.class);
    final AccessLevel defaultAccessLevel = AccessLevel.valueOf(Iterables.getFirst(defaultLevels, AccessLevel.NONE.name()));

    if (// If explicit visibility modifier is set - no point to continue.
        !hasPackagePrivateModifier(modifierList) ||
            // If @PackagePrivate is requested, leave the field as is
            PsiAnnotationSearchUtil.isAnnotatedWith(parentElement, lombok.experimental.PackagePrivate.class)) {
      return modifiers;
    }

    switch (defaultAccessLevel) {
      case PRIVATE:
        modifiers.add(PsiModifier.PRIVATE);
        break;

      case PROTECTED:
        modifiers.add(PsiModifier.PROTECTED);
        break;

      case PUBLIC:
        modifiers.add(PsiModifier.PUBLIC);
        break;

      default:
        // no-op
        break;
    }

    return modifiers;
  }

  ;

  private boolean hasPackagePrivateModifier(@NotNull PsiModifierList modifierList) {
    return !(modifierList.hasExplicitModifier(PsiModifier.PUBLIC) || modifierList.hasExplicitModifier(PsiModifier.PRIVATE) ||
        modifierList.hasExplicitModifier(PsiModifier.PROTECTED));
  }
}
