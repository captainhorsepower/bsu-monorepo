# Параллельные и распределённые вычисления

Первую лабо кое-как сдали, в основоном копипаст с Drapegnik. 
Нужно сделать ещё одну. 

Мне понравилось условия 4й: 

https://docviewer.yandex.ru/view/643359723/?*=VODtmjCq1oFiI5HfiTK0Fb1ZmXF7InVybCI6InlhLWRpc2stcHVibGljOi8vMHN4SGMxZnlRZTJycHZ4VFMzYTFnWk5vRDB3UWtjUmFzOUx2cVlaTXdRekZaZ21ERjA3V2FoVkpIdFFNb3ZLY3EvSjZicG1SeU9Kb25UM1ZvWG5EYWc9PSIsInRpdGxlIjoiTWFwUmVkdWNlLnppcCIsIm5vaWZyYW1lIjpmYWxzZSwidWlkIjoiNjQzMzU5NzIzIiwidHMiOjE2MDc3ODY5MTQxNjMsInl1IjoiNjk0NzMzOTc3MTU4MzMwNjI5NiJ9

Спросил, можно завести спарк.

## Запускаю спарк локально.

1. Скачал последний спарк, prebuilt for hadoop 2.7
2. и он просто запускается, демки работают. `spark-3.0.1-bin-hadoop2.7/bin/run-example SparkPi 10`

### Кто такой спарк?

Distributed (in-memory?) processing.
Он так же может использовать рапределённое хранилище (MESOS, Hadoop, YARN) для всяких фич. 
Но можно и без этого, как оказалось. Надеюсь

[programming guide](https://spark.apache.org/docs/latest/rdd-programming-guide.html#using-the-shell)

 ---
useful links:
- [k8s operator](https://github.com/radanalyticsio/spark-operator)
- [documentation for running on k8s](https://spark.apache.org/docs/latest/running-on-kubernetes.html#how-it-works)
- [spark-submit script](https://spark.apache.org/docs/latest/submitting-applications.html)
- [medium: running on local machine](https://medium.com/@sharifuli/running-spark-on-local-machine-c38957d022f4)
- [baeldung on spark]
- https://databricks.com/spark/getting-started-with-apache-spark/datasets
- https://archive.org/details/google_ngrams-eng-1M-1gram
- https://hub.docker.com/r/bitnami/spark/
- https://habr.com/ru/company/neoflex/blog/511734/