package ru.practicum.explorewithmeservice.helpers;

import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.apache.commons.lang3.function.TriFunction;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;


@NoArgsConstructor
public class Helper {
    final ModelMapper mapper = new ModelMapper();

    public <T, TT> @NonNull Function<T, TT> start(Function<? super T, ? extends TT> after) {
        Objects.requireNonNull(after);
        return (Function<T, TT>) after;
    }

    public <T1, T2, TT> @NonNull BiFunction<T1, T2, TT> start2(BiFunction<? super T1, ? super T2, ? extends TT> after) {
        Objects.requireNonNull(after);
        return after::apply;
    }

    public <T1, T2, T3, TT> @NonNull TriFunction<T1, T2, T3, TT> start3(TriFunction<? super T1, ? super T2, ? super T3, ? extends TT> after) {
        Objects.requireNonNull(after);
        return after::apply;
    }


    public <T, TT> @NonNull Function<TT, T> to(Class<T> c) {
        return in -> (T) mapper.map(in, c);
    }

    public <T> @NonNull Function<Object, T> run(Object o, String name) {
        return in -> {
            try {
                Class[] paramTypes = new Class[1];
                paramTypes[0] = in.getClass();
                Method method = o.getClass().getMethod(name, paramTypes);
                return (T) method.invoke(o, in);
            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                throw new RuntimeException(e);
            }
        };
    }

    public <T> @NonNull Function<Object, List<T>> runList(Object o, String name) {
        return in -> {
            try {
                Class[] paramTypes = new Class[1];
                paramTypes[0] = in.getClass();
                Method method = o.getClass().getMethod(name, paramTypes);
                return (List<T>) method.invoke(o, in);
            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                throw new RuntimeException(e);
            }
        };
    }

    public <T> @NonNull BiFunction<Object, Object, T> biRun(Object o, String name) {
        return (in1, in2) -> {
            try {
                Method method = o.getClass().getMethod(name, in1.getClass(), in2.getClass());
                return (T) method.invoke(o, in1, in2);
            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                throw new RuntimeException(e);
            }
        };
    }

    public String findEnclosing() {
        String methodName = "";
        final StackTraceElement[] stee = Thread.currentThread().getStackTrace();
        for (StackTraceElement ste : stee) {
            if (ste.getClassName().contains("controller")) {
                System.out.println(ste.getClassName() + "#" + ste.getMethodName());
                methodName = ste.getMethodName();
                break;
            }
        }
        return methodName;
    }

    public <T> @NonNull BiFunction<Object, Object, Page<T>> biRunList(Object o, String name) {
        return (in1, in2) -> {
            try {
                Method method = o.getClass().getMethod(name, in1.getClass(), in2.getClass());
                return (Page<T>) method.invoke(o, in1, in2);
            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                throw new RuntimeException(e);
            }
        };
    }

    public <T, TT> @NonNull Function<Page<T>, List<TT>> fromPage(Class<TT> c) {
        return in -> in.stream().map(
                m -> to(c).apply(m)
        ).collect(Collectors.toList());
    }

    public <T, TT> @NonNull Function<List<T>, List<TT>> fromList(Class<TT> c) {
        return in -> in.stream().map(
                m -> to(c).apply(m)
        ).collect(Collectors.toList());
    }
}
