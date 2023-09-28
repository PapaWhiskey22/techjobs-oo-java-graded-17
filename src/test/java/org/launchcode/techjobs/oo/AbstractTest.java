package org.launchcode.techjobs.oo;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by LaunchCode
 */
public class AbstractTest {

    protected Class getClassByName(String className) throws ClassNotFoundException {
        return Class.forName("org.launchcode.techjobs.oo." + className);
    }

    private Object initializeJobField(Class clazz, String value) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        Constructor constructor = clazz.getConstructor(String.class);
        return constructor.newInstance(value);
    }

    protected Job createJob(String name, String employer, String location, String positionType, String coreCompetency) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        Class jobClass = getClassByName("Job");
        Constructor jobConstructor = jobClass.getConstructor(
                String.class,
                Employer.class,
                Location.class,
                PositionType.class,
                CoreCompetency.class
        );

        return (Job) jobConstructor.newInstance(
                name,
                initializeJobField(Employer.class, employer),
                initializeJobField(Location.class, location),
                initializeJobField(PositionType.class, positionType),
                initializeJobField(CoreCompetency.class, coreCompetency));
    }

    protected String getJobFieldString(Job job, String fieldName, Boolean substituteEmpty) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException, ClassNotFoundException, NoSuchFieldException {
        Class jobClass = getClassByName("Job");
        String value;

        if (fieldName.equals("name")) {
            Field nameField = jobClass.getDeclaredField(fieldName);
            nameField.setAccessible(true);
            value = (String) nameField.get(job);
        } else {
            String fieldClassName = fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
            Class clazz = getClassByName(fieldClassName);
            Field field = jobClass.getDeclaredField(fieldName);
            field.setAccessible(true);
            Object fieldValue = field.get(job);
            Method getValueMethod = clazz.getMethod("getValue");
            value = (String) getValueMethod.invoke(fieldValue);
        }
        if (substituteEmpty && (value == null || value == "")) {
            value = "Data not available";
        }
        return value;
    }

    protected int getJobId(Job job) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException, NoSuchFieldException {
        Class jobClass = getClassByName("Job");
        Field idField = jobClass.getDeclaredField("id");
        idField.setAccessible(true);
        return idField.getInt(job);
    }

    protected String getJobString (Job job) throws NoSuchMethodException, ClassNotFoundException, InvocationTargetException, IllegalAccessException, NoSuchFieldException {
        String result = System.lineSeparator();

        result += "ID: " + getJobId(job) + System.lineSeparator();
        result += "Name: " + getJobFieldString(job, "name", true) + System.lineSeparator();
        result += "Employer: " + getJobFieldString(job, "employer", true) + System.lineSeparator();
        result += "Location: " + getJobFieldString(job, "location", true) + System.lineSeparator();
        result += "Position Type: " + getJobFieldString(job, "positionType", true) + System.lineSeparator();
        result += "Core Competency: " + getJobFieldString(job, "coreCompetency", true) + System.lineSeparator();

        return result;
    }

}
