package emovie.recommender;

import emovie.recommender.builder.BuilderSettings;

import java.lang.reflect.Method;

/**
 * Created with IntelliJ IDEA.
 * User: Lumbendil
 * Date: 31/08/13
 * Time: 23:14
 * To change this template use File | Settings | File Templates.
 */
public class ParameterTunner
{
    private SettingsRunner runner;

    public ParameterTunner(SettingsRunner runner) {
        this.runner = runner;
    }

    public float tuneParameter(BuilderSettings settings, String methodName, Class type, float value, float step, float maxValue, float minValue, float minimumStep, int iterations) throws Exception
    {
        double previousResult = Double.POSITIVE_INFINITY;
        int direction = 1;

        Method method = settings.getClass().getMethod(methodName, type);

        for (int i = 0; i < iterations; i++) {
            if (type == int.class) {
                method.invoke(settings, (int) value);
            } else {
                method.invoke(settings, value);
            }

            double result = runner.run(settings);
            if (result > previousResult) {
                direction *= -1;
                step /= 2;

                if (step < minimumStep) {
                    return value;
                }
            }
            previousResult = result;

            value += step * direction;

            if (value > maxValue) {
                value = maxValue;
            } else if (value < minValue) {
                value = minValue;
            }
        }

        return value;
    }
}
