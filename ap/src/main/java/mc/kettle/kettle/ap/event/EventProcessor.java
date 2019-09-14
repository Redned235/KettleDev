package mc.kettle.kettle.ap.event;

import com.google.common.collect.Sets;
import com.google.common.io.CharStreams;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.QualifiedNameable;
import javax.lang.model.element.TypeElement;
import javax.tools.FileObject;
import javax.tools.StandardLocation;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.Iterator;
import java.util.Set;

import static javax.tools.Diagnostic.Kind.*;

@SupportedAnnotationTypes("mc.kettle.kettle.event.RegisterEvent")
@SupportedSourceVersion(SourceVersion.RELEASE_8)
public class EventProcessor extends AbstractProcessor {
    private final Set<String> events = Sets.newHashSet();

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);

        try {
            FileObject file = processingEnv.getFiler().getResource(StandardLocation.CLASS_OUTPUT, "mc.kettle.kettle.event", "events.txt");

            try (Reader reader = file.openReader(false)) {
                events.addAll(CharStreams.readLines(reader));
            }

            processingEnv.getMessager().printMessage(NOTE, "Found " + events.size() + " events in " + file.getName());
        } catch (IOException ignored) {
        }
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        if (roundEnv.processingOver()) {
            try {
                FileObject file = processingEnv.getFiler().createResource(StandardLocation.CLASS_OUTPUT, "mc.kettle.kettle.event", "events.txt");
                Iterator<String> itr = events.iterator();

                while (itr.hasNext()) {
                    String event = itr.next();

                    int pos = event.lastIndexOf(':');
                    if (pos >= 0) {
                        if (!validateMethod(event.substring(0, pos), event.substring(pos + 1), annotations)) {
                            itr.remove();
                        }
                    } else {
                        if (!validateClass(event, annotations)) {
                            itr.remove();
                        }
                    }
                }

                processingEnv.getMessager().printMessage(NOTE,
                        "Writing " + events.size() + " events to " + file.getName());

                try (Writer w = file.openWriter()) {
                    for (String event : events) {
                        w.write(event);
                        w.write('\n');
                    }
                }
            } catch (IOException e) {
                processingEnv.getMessager().printMessage(ERROR, "Failed to write events to events.txt");
                e.printStackTrace();
            }

            return false;
        }

        for (TypeElement anno : annotations) {
            for (Element element : roundEnv.getElementsAnnotatedWith(anno)) {
                this.events.add(getQualifiedName(element));
            }
        }

        return true;
    }

    private boolean validateClass(String name, Set<? extends TypeElement> annotations) {
        name = name.replace('$', '.');
        TypeElement element = processingEnv.getElementUtils().getTypeElement(name);
        if (element != null) {
            return element.getAnnotationMirrors().containsAll(annotations);
        } else {
            processingEnv.getMessager().printMessage(WARNING, "Skipping unknown class: " + name);
            return false;
        }
    }

    private boolean validateMethod(String owner, String name, Set<? extends TypeElement> annotations) {
        TypeElement element = processingEnv.getElementUtils().getTypeElement(owner);
        if (element != null) {
            for (Element enclosed : element.getEnclosedElements()) {
                if (enclosed.getKind() == ElementKind.METHOD && enclosed.getSimpleName().toString().equals(name)
                        && enclosed.getAnnotationMirrors().containsAll(annotations)) {
                    return true;
                }
            }
        } else {
            processingEnv.getMessager().printMessage(WARNING, "Skipping unknown method owner: " + owner);
        }

        return false;
    }

    private static String getQualifiedName(Element element) {
        switch (element.getKind()) {
            case CLASS:
                if (element.getEnclosingElement().getKind() == ElementKind.CLASS) {
                    return getQualifiedName(element.getEnclosingElement()) + '$' + element.getSimpleName();
                } else {
                    return ((QualifiedNameable) element).getQualifiedName().toString();
                }
            case METHOD:
                return getQualifiedName(element.getEnclosingElement()) + ':' + element.getSimpleName().toString();
            default:
                throw new UnsupportedOperationException(element.getClass().toString());
        }
    }
}