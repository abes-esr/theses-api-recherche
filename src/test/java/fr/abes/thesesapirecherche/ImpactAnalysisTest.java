package fr.abes.thesesapirecherche;

import com.tngtech.archunit.core.domain.*;
import com.tngtech.archunit.core.importer.*;
import org.junit.jupiter.api.Test;
import java.util.*;

class ImpactAnalysisTest {

    
    // classes you changed (or generate this list from `git diff --name-only`)
    static final Set<String> CHANGED = Set.of(
            "fr.abes.thesesapirecherche.theses.model.These");

    @Test
    void whichEndpointsAreImpacted() {
        JavaClasses classes = new ClassFileImporter()
                .withImportOption(new ImportOption.DoNotIncludeTests())
                .importPackages("fr.abes.thesesapirecherche");

        // BFS backwards: who depends on the changed classes, transitively?
        Map<JavaClass, JavaClass> via = new HashMap<>();   // class -> the class it reached us through
        Deque<JavaClass> queue = new ArrayDeque<>();
        for (String name : CHANGED) {
            JavaClass c = classes.get(name);
            via.put(c, null);
            queue.add(c);
        }
        while (!queue.isEmpty()) {
            JavaClass cur = queue.poll();
            for (Dependency d : cur.getDirectDependenciesToSelf()) {
                JavaClass origin = d.getOriginClass();
                if (via.containsKey(origin)) continue;
                via.put(origin, cur);
                queue.add(origin);
            }
        }

        Set<JavaClass> reached = via.keySet();

        // Report controller methods that directly touch an impacted class
        for (JavaClass c : reached) {
            if (!isController(c)) continue;
            System.out.println("\n" + c.getName() + "   (path: " + chain(c, via) + ")");
            for (JavaMethod m : c.getMethods()) {
                String mapping = mapping(m);
                if (mapping == null) continue;
                boolean touches =
                        m.getAccessesFromSelf().stream().anyMatch(a -> reached.contains(a.getTargetOwner()))
                                || m.getRawParameterTypes().stream().anyMatch(reached::contains)
                                || reached.contains(m.getRawReturnType());
                if (touches) System.out.println("   IMPACTED  " + mapping + "  ->  " + m.getName());
            }
        }
    }

    static boolean isController(JavaClass c) {
        return c.isAnnotatedWith("org.springframework.web.bind.annotation.RestController")
                || c.isAnnotatedWith("org.springframework.stereotype.Controller");
    }

    static String mapping(JavaMethod m) {
        return m.getAnnotations().stream()
                .filter(a -> a.getRawType().getName().startsWith("org.springframework.web.bind.annotation.")
                        && a.getRawType().getSimpleName().endsWith("Mapping"))
                .map(a -> a.getRawType().getSimpleName() + " " +
                        a.get("value").map(v -> Arrays.deepToString((Object[]) v)).orElse(""))
                .findFirst().orElse(null);
    }

    static String chain(JavaClass c, Map<JavaClass, JavaClass> via) {
        StringBuilder sb = new StringBuilder(c.getSimpleName());
        for (JavaClass p = via.get(c); p != null; p = via.get(p)) sb.append(" -> ").append(p.getSimpleName());
        return sb.toString();
    }
}
