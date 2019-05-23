package com.zaozaofan.lib;


import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.Scheduler;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.observables.GroupedObservable;
import io.reactivex.schedulers.Schedulers;

/**
 * RxJava2 只看这一篇文章就够了
 * https://juejin.im/post/5b17560e6fb9a01e2862246f#heading-11
 */
public class RxJava1 {
    public static final String TAG = "Rxjava1";
    private static int i;
    private static Long l;

    public static void main(String[] args) throws InterruptedException {
//        just();
//        fromArray();
//        fromCallable();
//        fromFuture();
//        fromIterable();
//        defer();
//        timer();
//        interval();
//        Rxjava1 ==============onSubscribe
//        Rxjava1 ==============clazz com.zaozaofan.lib.RxJava1$14
//        Rxjava1 ==============Thread main
//
//        Process finished with exit code 0   直接退出了.jvm情况下.
//        intervalRange();
//        range();
        //测试方法1
//        Thread.sleep(6000);

//        error();
//        map();
//        flatMap();

//        concatMap();

//        Thread.sleep(6000);


//        buffer();

//        groupBy();
        scan();
    }

    static void create() {
        //create observable 创建被观察者
        Observable observable = Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> e) throws Exception {
                //订阅时处理事件并发送给观察者
                System.out.println("=========================currentThread name: " + Thread.currentThread().getName());
                e.onNext(1);
                e.onNext(2);
                e.onNext(3);
                e.onComplete();
                e.onNext(3);
            }
        });

        //create observer 创建观察者
        Observer observer = new Observer<Integer>() {
            @Override
            public void onSubscribe(Disposable d) {
                System.out.println("onSubscribe");

            }

            @Override
            public void onNext(Integer o) {
                System.out.println("onNext");
            }

            @Override
            public void onError(Throwable e) {
                System.out.println("onError");
            }

            @Override
            public void onComplete() {
                System.out.println("onComplete");
            }
        };

        observable.subscribe(observer);

//        onSubscribe
//                =========================currentThread name: main
//                onNext
//        onNext
//                onNext
//        onComplete

        //总结
        //1.create observable  //实现接口ObservableOnSubscribe subscribe方法,回调,1个方法   异步,同步,流编程,调用方不管同步还是异步.
        //2.create observer  //实现被observable的回调 onSubscribe onNext(T t)泛型数据 onError  onComplete 4个方法
        //3.subscribe
    }


    /**
     * 创建一个被观察者，并发送事件，发送的事件不可以超过10个以上。
     */
    static void just() {
        Observable.just(1, 2, 3)
                .subscribe(new Observer<Integer>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        System.out.println("onSubscribe");
                    }

                    @Override
                    public void onNext(Integer integer) {
                        System.out.println("onNext " + integer);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    static void fromArray() {
        Integer array[] = {1, 2, 3, 4};
        Observable.fromArray(array)
                .subscribe(new Observer<Integer>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        System.out.println("onSubscribe");
                    }

                    @Override
                    public void onNext(Integer integer) {
                        System.out.println("onNext " + integer);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    static void fromCallable() {
//        Observable.fromCallable(new Callable<Integer>() {
//            @Override
//            public Integer call() throws Exception {
//                return 1;
//            }
//        }) .subscribe(new Observer<Integer>() {
//            @Override
//            public void onSubscribe(Disposable d) {
//                System.out.println("onSubscribe");
//            }
//
//            @Override
//            public void onNext(Integer integer) {
//                System.out.println("onNext "+integer);
//            }
//
//            @Override
//            public void onError(Throwable e) {
//
//            }
//
//            @Override
//            public void onComplete() {
//
//            }
//        });

//        这里的 Callable 是 java.util.concurrent 中的 Callable，Callable 和 Runnable 的用法基本一致，只是它会返回一个结果值，这个结果值就是发给观察者的。

        //这些都是为了传递函数,不如用本身支持函数传参的语言.kotlin.
        Observable.fromCallable(new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                return 1;
            }
        })
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        System.out.println("================accept " + integer);
                    }
                });


    }

    static void fromFuture() {
        //future task  函数定义和调用分开. 为了函数式编程,生成了好多匿名类,其实只需要函数.
        FutureTask<String> futureTask = new FutureTask<>(new Callable<String>() {
            @Override
            public String call() throws Exception {
                System.out.println("CallableDemo is Running");
                return "返回结果";
            }
        });

        Observable.fromFuture(futureTask)
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        //doOnSubscribe() 的作用就是只有订阅时才会发送事件
                        futureTask.run();
                    }
                })
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        System.out.println("================accept " + s);
                    }
                });

//        futureTask.run(); //只能执行一次
//        CallableDemo is Running
//                ================accept 返回结果
    }


    static void fromIterable() {
        List<Integer> list = new ArrayList<>();
        list.add(0);
        list.add(1);
        list.add(2);
        list.add(3);
        Observable.fromIterable(list)
                .subscribe(new Observer<Integer>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d(TAG, "=================onSubscribe");
                    }

                    @Override
                    public void onNext(Integer integer) {
                        Log.d(TAG, "=================onNext " + integer);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "=================onError ");
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "=================onComplete ");
                    }
                });

    }

    static void defer() {
        // i 要定义为成员变量
        i = 100;

        Observable<Integer> observable = Observable.defer(new Callable<ObservableSource<? extends Integer>>() {
            @Override
            public ObservableSource<? extends Integer> call() throws Exception {
                return Observable.just(i); //每次订阅都调用一遍,订阅时调用创建ObservableSource
            }
        });

        i = 200;

        Observer observer = new Observer<Integer>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(Integer integer) {
                Log.d(TAG, "================onNext " + integer);
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        };

        observable.subscribe(observer); //200

        i = 300;

        observable.subscribe(observer);//300

    }


    /**
     * 当到指定时间后就会发送一个 0L 的值给观察者。
     */
    static void timer() {
        System.out.println("timer");
        Observable.timer(1, TimeUnit.SECONDS)  //定义被观察者
                .subscribe(new Observer<Long>() {  //定义观察者,订阅者者
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Long aLong) {
                        Log.d(TAG, "===============onNext " + aLong);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
        //没生效
    }

    /**
     * 每隔一段时间就会发送一个事件，这个事件是从0开始，不断增1的数字。
     * onSubscribe在订阅线程,onNext在工作线程,无法测试
     */
    public static void interval() throws InterruptedException {
        final CountDownLatch latch = new CountDownLatch(1);
        Log.d(TAG, "==============interval " + Thread.currentThread().getName());
        //Schedulers.io() 是main函数
        Observable.interval(1, TimeUnit.SECONDS, Schedulers.io()) //,Schedulers.io() //  return interval(period, period, unit, Schedulers.computation());
                .subscribe(new Observer<Long>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d(TAG, "==============onSubscribe " + Thread.currentThread().getName()); // main
//                        Log.d(TAG, "==============clazz "+this.getClass().getName());//clazz com.zaozaofan.lib.RxJava1$14
                    }

                    @Override
                    public void onNext(Long aLong) {
                        Log.d(TAG, "==============onNext " + aLong + " " + Thread.currentThread().getName());
                        //默认,Schedulers.computation() onNext 0 RxComputationThreadPool-1
                        //Schedulers.io() onNext 0 RxCachedThreadScheduler-1


                        l = aLong;
                        //判断subscribe是否已经取消订阅 并且 l 的值已经等于3
                        if (l == 10) {
//                            onComplete(); //自己调用没有用的
                            //1.Schedulers.shutdown();
                            //2. subscription.unsubscribe();
//                            Schedulers.shutdown();
                            latch.countDown();
                        }

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });


        //3、 当前线程挂起等待
        latch.await();
        System.out.println("主线程执行完毕....");

    }


    /**
     * 可以指定发送事件的开始值和数量，其他与 interval() 的功能一样。
     */
    public static void intervalRange() {
        Observable.intervalRange(2, 5, 2, 1, TimeUnit.SECONDS)
                .subscribe(new Observer<Long>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d(TAG, "==============onSubscribe ");
                    }

                    @Override
                    public void onNext(Long aLong) {
                        Log.d(TAG, "==============onNext " + aLong);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }

    /**
     * 同时发送一定范围的事件序列。
     * 都在主线程,可以测试
     */
    public static void range() {
        Observable.range(2, 5)
                .subscribe(new Observer<Integer>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d(TAG, "==============onSubscribe " + Thread.currentThread());
                    }

                    @Override
                    public void onNext(Integer aLong) {
                        Log.d(TAG, "==============onNext " + aLong + " " + Thread.currentThread());
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }


    public static void error() {
        //empty() ： 直接发送 onComplete() 事件
        //never()：不发送任何事件
        //error()：发送 onError() 事件
        Observable.error(new Throwable())
                .subscribe(new Observer<Object>() {

                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d(TAG, "==================onSubscribe");
                    }

                    @Override
                    public void onNext(Object o) {
                        Log.d(TAG, "==================onNext");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "==================onError " + e);
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "==================onComplete");
                    }
                });


    }


    public static void map() {
        Observable.just(1, 2, 3)
                .map(integer -> "I'm " + integer)
                .subscribe(new Observer < String > () {
                    @Override
                    public void onSubscribe(Disposable d) {
//                        Log.e(TAG, "===================onSubscribe");
                    }

                    @Override
                    public void onNext(String s) {
                        Log.e(TAG, "===================onNext " + s);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }


    /**
     * 这个方法可以将事件序列中的元素进行整合加工，返回一个新的被观察者。
     */
    public static void flatMap() {
        //2person * 2plan *2 action = 8
        List<Person> personList = new ArrayList<>();
        for(int i=0;i<2;i++){
            List<Plan> planList = new ArrayList<>();
            for(int j=0;j<2;j++){
                List<String> planActionList = new ArrayList<>();
                for(int k=0;k<2;k++){
                    planActionList.add("person "+i+" plan "+j+"action "+k);
                }
                Plan plan =new Plan("time","content "+i+" "+j);
                plan.setActionList(planActionList);
                planList.add(plan);
            }
            Person person = new Person("person "+i,planList);
            personList.add(person);
        }
        for (Person person:personList){
            for(Plan plan : person.getPlanList()){
                for (String action: plan.getActionList()) {
                    Log.d(TAG, "for ==================action " + action);
                }
            }
        }

//        Observable.fromIterable(personList)
//                .map(person -> person.getPlanList())
//                .subscribe(new Observer < List < Plan >> () {
//                    @Override
//                    public void onSubscribe(Disposable d) {
//
//                    }
//
//                    @Override
//                    public void onNext(List < Plan > plans) {
//                        for (Plan plan: plans) {
//                            List < String > planActionList = plan.getActionList();
//                            for (String action: planActionList) {
//                                Log.d(TAG, "==================action " + action);
//                            }
//                        }
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//
//                    }
//
//                    @Override
//                    public void onComplete() {
//
//                    }
//                });

        Observable.fromIterable(personList)
                .flatMap(new Function < Person, ObservableSource < Plan >> () {
                    @Override
                    public ObservableSource < Plan > apply(Person person) {
                        return Observable.fromIterable(person.getPlanList());
                    }
                })
                .flatMap(new Function < Plan, ObservableSource < String >> () {
                    @Override
                    public ObservableSource < String > apply(Plan plan) throws Exception {
                        return Observable.fromIterable(plan.getActionList());
                    }
                })
                .subscribe(new Observer < String > () {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(String s) {
                        Log.d(TAG, "==================action: " + s+" "+Thread.currentThread().getName());
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });


    }


    public static void concatMap(){
        //2person * 2plan *2 action = 8
        List<Person> personList = new ArrayList<>();
        for(int i=0;i<2;i++){
            List<Plan> planList = new ArrayList<>();
            for(int j=0;j<2;j++){
                List<String> planActionList = new ArrayList<>();
                for(int k=0;k<2;k++){
                    planActionList.add("person "+i+" plan "+j+"action "+k);
                }
                Plan plan =new Plan("time","content "+i+" "+j);
                plan.setActionList(planActionList);
                planList.add(plan);
            }
            Person person = new Person("person "+i,planList);
            personList.add(person);
        }


        Observable.fromIterable(personList)
                .concatMap(new Function < Person, ObservableSource < Plan >> () {  //flatMap
                    @Override
                    public ObservableSource < Plan > apply(Person person) {
                        if ("person 0".equals(person.getName())) {
                            return Observable.fromIterable(person.getPlanList()).delay(2, TimeUnit.MILLISECONDS);
                        }
                        return Observable.fromIterable(person.getPlanList());
                    }
                })
                .subscribe(new Observer < Plan > () {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Plan plan) {
                        Log.d(TAG, "==================plan " + plan.getContent());
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });

         //delay 情况下
//        Rxjava1 ==================plan content 1 0
//        Rxjava1 ==================plan content 1 1
//        Rxjava1 ==================plan content 0 0
//        Rxjava1 ==================plan content 0 1

    }


    /**
     * 从需要发送的事件当中获取一定数量的事件，并将这些事件放到缓冲区当中一并发出。
     * 从结果可以看出，每次发送事件，指针都会往后移动一个元素再取值，直到指针移动到没有元素的时候就会停止取值
     */
    public static void buffer(){
        Observable.just(1, 2, 3, 4, 5)
                .buffer(2, 2)
                .subscribe(new Observer < List < Integer >> () {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(List < Integer > integers) {
                        Log.d(TAG, "================缓冲区大小： " + integers.size());
                        for (Integer i: integers) {
                            Log.d(TAG, "================元素： " + i);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }

    public static void groupBy(){
        Observable.just(5, 2, 3, 4, 1, 6, 8, 9, 7, 10)
                .groupBy(new Function < Integer, Integer > () {
                    @Override
                    public Integer apply(Integer integer) throws Exception {
                        return integer % 3; //0,1,2 3组 //每返回一个值，那就代表会创建一个组
                    }
                })
                .subscribe(new Observer < GroupedObservable < Integer, Integer >> () {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d(TAG, "====================onSubscribe ");
                    }

                    @Override
                    public void onNext(GroupedObservable< Integer, Integer > integerIntegerGroupedObservable) {
                        //发射的是分组观察者
                        Log.d(TAG, "====================onNext ");
                        integerIntegerGroupedObservable.subscribe(new Observer < Integer > () {
                            @Override
                            public void onSubscribe(Disposable d) {
                                Log.d(TAG, "====================GroupedObservable onSubscribe ");
                            }

                            @Override
                            public void onNext(Integer integer) {
                                Log.d(TAG, "====================GroupedObservable onNext  groupName: " + integerIntegerGroupedObservable.getKey() + " value: " + integer);
                            }

                            @Override
                            public void onError(Throwable e) {
                                Log.d(TAG, "====================GroupedObservable onError ");
                            }

                            @Override
                            public void onComplete() {
                                Log.d(TAG, "====================GroupedObservable onComplete ");
                            }
                        });
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "====================onError ");
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "====================onComplete ");
                    }
                });

    }

    public static void scan(){
        Observable.just(1, 2, 3)
                .scan(new BiFunction< Integer, Integer, Integer >() {
                    @Override
                    public Integer apply(Integer integer, Integer integer2) throws Exception {
                        Log.d(TAG, "====================apply ");
                        Log.d(TAG, "====================integer " + integer);
                        Log.d(TAG, "====================integer2 " + integer2);
                        return integer + integer2;
                    }
                })
                .subscribe(new Consumer < Integer > () {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        Log.d(TAG, "====================accept " + integer);
                    }
                });

    }
}
