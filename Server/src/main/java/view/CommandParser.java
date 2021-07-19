package view;

import model.message.Message;
import model.message.MessageTag;
import view.annotation.Instruction;
import view.annotation.Label;
import view.annotation.Tag;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Arrays;

public class CommandParser {
    private static final CommandParser COMMAND_PARSER = new CommandParser();

    private CommandParser() {
    }

    public static CommandParser getInstance() {
        return COMMAND_PARSER;
    }

    public Object parseCommand(Message message, ServerController serverController) {
        Class<?> myClass = serverController.getClass();
        ArrayList<Method> methodsWithInstruction = getMethodsAnnotatedWithInstruction(new ArrayList<>(Arrays.asList(myClass.getDeclaredMethods())),
                message.getMessageInstruction().getName());
        ArrayList<Method> methodsWithLabel;
        methodsWithLabel = getMethodsAnnotatedWithLabel(methodsWithInstruction, message.getMessageLabel().getName());
        ArrayList<Method> methodsWithTag = methodsWithLabel;
        for (MessageTag tag : message.getMessageTags())
            methodsWithTag = getMethodsWithParametersAnnotatedWithTag(methodsWithLabel, tag.getName(), message.getMessageTags().length);

        try {
            Method methodToInvoke = methodsWithTag.get(0);
            methodToInvoke.setAccessible(true);
            return methodToInvoke.invoke(serverController, message.getOrderedTagValues());
        } catch (Exception exception) {
            Responses.logToConsole(Responses.WRONG_FORMAT);
            exception.printStackTrace();
            return Responses.WRONG_FORMAT;
        }
    }


    private ArrayList<Method> getMethodsAnnotatedWithInstruction(final ArrayList<Method> allMethods, String value) {
        final ArrayList<Method> methods = new ArrayList<>();
        for (final Method method : allMethods) {
            if (method.isAnnotationPresent(Instruction.class)) {
                Instruction instruction = (Instruction) method.getAnnotation((Class<? extends Annotation>) Instruction.class);
                if (instruction.value().equals(value)) {
                    methods.add(method);
                }
            }
        }
        return methods;
    }

    private ArrayList<Method> getMethodsAnnotatedWithLabel(final ArrayList<Method> allMethods, String value) {
        final ArrayList<Method> methods = new ArrayList<>();
        for (final Method method : allMethods) {
            if (method.isAnnotationPresent(Label.class)) {
                Label instruction = (Label) method.getAnnotation((Class<? extends Annotation>) Label.class);
                if (instruction.value().equals(value)) {
                    methods.add(method);
                }
            }
        }
        return methods;
    }

    private ArrayList<Method> getMethodsWithParametersAnnotatedWithTag(final ArrayList<Method> allMethods, String value, int parameterCount) {
        final ArrayList<Method> methods = new ArrayList<>();
        for (final Method method : allMethods) {
            if (method.getParameterCount() != parameterCount)
                continue;
            for (Parameter parameter : method.getParameters()) {
                if (parameter.isAnnotationPresent(Tag.class)) {
                    Tag annotationInstance = (Tag) parameter.getAnnotation((Class<? extends Annotation>) Tag.class);
                    if (annotationInstance.value().equals(value)) {
                        methods.add(method);
                        return methods;
                    }
                }
            }
        }

        return methods;
    }


}
