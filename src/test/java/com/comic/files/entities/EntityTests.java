package com.comic.files.entities;

import com.comic.files.dto.DUser;
import com.comic.files.model.AcceptedObjects;
import org.junit.jupiter.api.Test;

import javax.persistence.Id;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class EntityTests {

    private final String EQUALS = "equals";
    private final String COMPARE_TO = "compareTo";
    private final AcceptedObjects[] acceptedObjects;

    /**
     * Test entity creator with the list of objects to test and fields to ignore on this objects.
     */
    public EntityTests() {

        acceptedObjects = new AcceptedObjects[] {
                new AcceptedObjects(DUser.class, Collections.emptyList())
        };
    }

    /**
     * Test Main Function.
     *
     * @throws InvocationTargetException
     * @throws InstantiationException
     * @throws IllegalAccessException
     */
    @Test
    public void entitiesTest() throws InvocationTargetException, InstantiationException, IllegalAccessException {
        for (AcceptedObjects acceptedObject : acceptedObjects) {
            if(acceptedObject.getObject().isEnum()) {
                testEnum(acceptedObject.getObject());
            } else {
                testObject(acceptedObject);
            }
        }
    }

    /**
     * Test Standart Object.
     *
     * @param acceptedObjects Class to be tested and ignored methods.
     *
     * @throws InvocationTargetException
     * @throws InstantiationException
     * @throws IllegalAccessException
     */
    private void testObject(AcceptedObjects acceptedObjects) throws InvocationTargetException, InstantiationException, IllegalAccessException {
        testConstructors(acceptedObjects.getObject());
        testFields(acceptedObjects.getObject());
        testMethods(acceptedObjects.getObject(), getInstance(acceptedObjects.getObject().getConstructors()[0]), acceptedObjects.getIgnoreMethods());
    }

    /**
     * Test Enum Object.
     *
     * @param object Class to be tested.
     *
     * @throws InvocationTargetException
     * @throws InstantiationException
     * @throws IllegalAccessException
     */
    private void testEnum(Class object) throws InvocationTargetException, InstantiationException, IllegalAccessException {
        for (Object instance : object.getEnumConstants()) {
            testMethods(object, instance, new ArrayList<>());
        }
    }

    /**
     * Test all constructors from the object.
     *
     * @param object Class to be tested.
     *
     * @throws InvocationTargetException
     * @throws InstantiationException
     * @throws IllegalAccessException
     */
    @SuppressWarnings("unchecked")
    private void testConstructors(Class object) throws InvocationTargetException, InstantiationException, IllegalAccessException {
        Constructor[] constructorList = object.getDeclaredConstructors();
        for (Constructor constructor : constructorList) {
            constructor.setAccessible(Boolean.TRUE);
            List fields = new ArrayList<>();
            for(int x = 0; x < constructor.getParameterCount(); x++) {
                fields.add(null);
            }
            if(fields.isEmpty()) {
                constructor.newInstance();
            } else {
                constructor.newInstance(fields.toArray());
            }
        }
    }

    /**
     * Test fields of objects.
     *
     * @param object Class to be tested.
     *
     * @throws IllegalAccessException
     */
    private void testFields(Class object) throws IllegalAccessException {
        for (Field declaredField : object.getDeclaredFields()) {
            if (Modifier.isPublic(object.getModifiers()) && Modifier.isStatic(object.getModifiers())) {
                declaredField.get(null);
            }
        }
    }

    /**
     * Test all methods of object.
     *
     * @param object Class to be tested.
     * @param instance Instance of object.
     * @param ignoreMethods Methods to be ignored.
     *
     * @throws InvocationTargetException
     * @throws InstantiationException
     * @throws IllegalAccessException
     */
    private void testMethods(Class object, Object instance, List<String> ignoreMethods) throws InvocationTargetException, InstantiationException, IllegalAccessException {


        for (Method testableMethod : getTestableMethods(object)) {
            List fields = getInjectionFields(testableMethod, object, instance);
            if( (!Modifier.isStatic(testableMethod.getModifiers())) && (!ignoreMethods.contains(testableMethod.getName())) ) {
                testableMethod.setAccessible(Boolean.TRUE);
                testableMethod.invoke(instance,fields.toArray());
                if(EQUALS.equals(testableMethod.getName())) {
                    testableMethod.invoke(instance,"");
                }
                if(COMPARE_TO.equals(testableMethod.getName())) {
                    testableMethod.invoke(instance, new Object[]{null});
                }
            }
        }
        
    }

    /**
     * List all testable methods.
     *
     * @param object Class to be tested.
     *
     * @return List of testable methods.
     */
    private List<Method> getTestableMethods(Class object) {
        List<Method> methods = new ArrayList<>();
        methods.addAll(Arrays.asList(object.getDeclaredMethods()));
        return methods.stream().filter(method -> !method.isSynthetic()).collect(Collectors.toList());
    }

    /**
     * Get instance of object based in a specific constructor.
     *
     * @param constructor Object constructor.
     *
     * @return Instance of object.
     *
     * @throws InvocationTargetException
     * @throws InstantiationException
     * @throws IllegalAccessException
     */
    private Object getInstance(Constructor constructor) throws InvocationTargetException, InstantiationException, IllegalAccessException {
        Object instance;
        if (constructor.getParameters().length > 0) {
            Object[] lObject = new Object[constructor.getParameters().length];
            instance = constructor.newInstance(lObject);
        } else {
            instance = constructor.newInstance();
        }
        return instance;
    }

    /**
     * Inject fields into the method.
     *
     * @param method Method to inject.
     * @param object Class to be tested.
     * @param instance Instance of object.
     *
     * @return List of fields injected.
     *
     * @throws IllegalAccessException
     */
    private List getInjectionFields(Method method, Class object, Object instance) throws IllegalAccessException {
        List fields = new ArrayList<>();
        for (Class<?> parameterType : method.getParameterTypes()) {
            if(parameterType.equals(object)) {
                setId(object,instance);
                fields.add(instance);
            } else {
                fields.add(null);
            }
        }
        return fields;
    }

    /**
     * Set ID field.
     *
     * @param classe Class to be tested.
     * @param instance Instance of object.
     *
     * @throws IllegalAccessException
     */
    private void setId(Class classe, Object instance) throws IllegalAccessException {
        for (Field field : classe.getDeclaredFields()) {
            if (field.getDeclaredAnnotation(Id.class) != null) {
                field.setAccessible(Boolean.TRUE);
                field.set(instance, 1L);
            }
        }
    }
    
}
