package com.service.processor;

import java.lang.annotation.Annotation;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.ErrorType;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.type.TypeVariable;
import javax.lang.model.util.ElementFilter;
import javax.lang.model.util.SimpleElementVisitor6;
import javax.lang.model.util.SimpleTypeVisitor6;

/**
 * @author dengxiaoqiu
 */
final class AnnotationUtils {

    public static AnnotationMirror getAnnotationMirror(Element element, Class<? extends Annotation> annotationClass) {
        String annotationClassName = annotationClass.getCanonicalName();
        for (AnnotationMirror annotationMirror : element.getAnnotationMirrors()) {
            TypeElement annotationTypeElement = asType(annotationMirror.getAnnotationType().asElement());
            if (annotationTypeElement.getQualifiedName().contentEquals(annotationClassName)) {
                return annotationMirror;
            }
        }
        return null;
    }

    public static TypeElement asTypeElement(TypeMirror mirror) {
        return asType(asElement(mirror));
    }

    public static Element asElement(TypeMirror typeMirror) {
        return typeMirror.accept(AsElementVisitor.INSTANCE, null);
    }

    private static final class AsElementVisitor extends SimpleTypeVisitor6<Element, Void> {
        private static final AsElementVisitor INSTANCE = new AsElementVisitor();

        @Override
        protected Element defaultAction(TypeMirror e, Void p) {
            throw new IllegalArgumentException(e + " cannot be converted to an Element");
        }

        @Override
        public Element visitDeclared(DeclaredType t, Void p) {
            return t.asElement();
        }

        @Override
        public Element visitError(ErrorType t, Void p) {
            return t.asElement();
        }

        @Override
        public Element visitTypeVariable(TypeVariable t, Void p) {
            return t.asElement();
        }
    }

    public static TypeElement asType(Element element) {
        return element.accept(TypeElementVisitor.INSTANCE, null);
    }

    private static final class TypeElementVisitor extends CastingElementVisitor<TypeElement> {
        private static final TypeElementVisitor INSTANCE = new TypeElementVisitor();

        TypeElementVisitor() {
            super("type element");
        }

        @Override
        public TypeElement visitType(TypeElement e, Void ignore) {
            return e;
        }
    }

    private abstract static class CastingElementVisitor<T> extends SimpleElementVisitor6<T, Void> {
        private final String label;

        CastingElementVisitor(String label) {
            this.label = label;
        }

        @Override
        protected final T defaultAction(Element e, Void ignore) {
            throw new IllegalArgumentException(e + " does not represent a " + label);
        }
    }

    public static DeclaredType asDeclared(TypeMirror maybeDeclaredType) {
        return maybeDeclaredType.accept(DeclaredTypeVisitor.INSTANCE, null);
    }

    private static final class DeclaredTypeVisitor extends CastingTypeVisitor<DeclaredType> {
        private static final DeclaredTypeVisitor INSTANCE = new DeclaredTypeVisitor();

        DeclaredTypeVisitor() {
            super("declared type");
        }

        @Override
        public DeclaredType visitDeclared(DeclaredType type, Void ignore) {
            return type;
        }
    }

    private abstract static class CastingTypeVisitor<T> extends SimpleTypeVisitor6<T, Void> {
        private final String label;

        CastingTypeVisitor(String label) {
            this.label = label;
        }

        @Override
        protected T defaultAction(TypeMirror e, Void v) {
            throw new IllegalArgumentException(e + " does not represent a " + label);
        }
    }

    public static AnnotationValue getAnnotationValue(AnnotationMirror annotationMirror, String elementName) {
        return getAnnotationElementAndValue(annotationMirror, elementName).getValue();
    }

    public static Map.Entry<ExecutableElement, AnnotationValue> getAnnotationElementAndValue(AnnotationMirror annotationMirror, final String elementName) {
        checkNotNull(annotationMirror);
        checkNotNull(elementName);

        for (Map.Entry<ExecutableElement, AnnotationValue> entry :
                getAnnotationValuesWithDefaults(annotationMirror).entrySet()) {
            if (entry.getKey().getSimpleName().contentEquals(elementName)) {
                return entry;
            }
        }

        throw new IllegalArgumentException(String.format("@%s does not define an element %s()",
                asType(annotationMirror.getAnnotationType().asElement()).getQualifiedName(),
                elementName));
    }

    public static Map<ExecutableElement, AnnotationValue> getAnnotationValuesWithDefaults(AnnotationMirror annotation) {
        Map<ExecutableElement, AnnotationValue> values = new LinkedHashMap<>();
        Map<? extends ExecutableElement, ? extends AnnotationValue> declaredValues = annotation.getElementValues();
        for (ExecutableElement method : ElementFilter.methodsIn(annotation.getAnnotationType().asElement().getEnclosedElements())) {
            if (declaredValues.containsKey(method)) {
                values.put(method, declaredValues.get(method));
            } else if (method.getDefaultValue() != null) {
                values.put(method, method.getDefaultValue());
            } else {
                throw new IllegalStateException(
                        "Unset annotation value without default should never happen: "
                                + asType(method.getEnclosingElement()).getQualifiedName()
                                + '.' + method.getSimpleName() + "()");
            }
        }
        return values;
    }

    public static <T> T checkNotNull(T reference) {
        if (reference == null) {
            throw new NullPointerException();
        }
        return reference;
    }
}
