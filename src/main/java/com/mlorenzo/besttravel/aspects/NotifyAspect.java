package com.mlorenzo.besttravel.aspects;

import com.mlorenzo.besttravel.annotations.Notify;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Arrays;


@Component
@Aspect
public class NotifyAspect {

    // Intercepta los métodos anotados con nuestra anotación personalizada "Notify" y, después de la ejecución de
    // esos métodos, ejecuta este método de forma automática
    @After(value = "@annotation(com.mlorenzo.besttravel.annotations.Notify)")
    public void notifyInFile(final JoinPoint joinPoint) throws IOException {
        // Obtiene el único argumento de entrada que recibe los métodos anotados con la anotación "Notify"
        final Pageable pageable = (Pageable) joinPoint.getArgs()[0];
        // Accedemos a la notificación "Notify" para poder obtener el contenido de su valor después
        final Notify annotation = ((MethodSignature) joinPoint.getSignature()).getMethod()
                .getAnnotation(Notify.class);
        try(final FileWriter fw = new FileWriter("files/notify.txt", true);
            final BufferedWriter bw = new BufferedWriter(fw)) {
            bw.write(String.format("At %s new %s with size page %d and order %s\n",
                    LocalDateTime.now(),annotation.value(), pageable.getPageSize(), pageable.getSort()));
        }
    }
}
