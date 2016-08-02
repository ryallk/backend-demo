; to make Cursive able to resolve symbols
(require '[boot.core :refer :all])

(set-env!
  :project 'demo-backend
  :version "0.1.0"
  :dependencies
  '[[org.clojure/clojure "1.8.0"]

    [com.datomic/datomic-pro "0.9.5350"]
    [com.amazonaws/aws-java-sdk-dynamodb "1.9.39"
     :exclusions [joda-time]]

    [camel-snake-kebab "0.3.2"]

    ;make sure FE/BE same version
    [hoplon/castra "3.0.0-alpha4"]
    [com.stuartsierra/component "0.3.1"]
    [org.danielsz/system "0.3.0"]
    [environ "1.0.1"]
    [danielsz/boot-environ "0.0.5"]
    [ring "1.4.0"]
    [ring/ring-defaults "0.1.5"]
    [ring.middleware.conditional "0.2.0"]
    [jumblerg/ring.middleware.cors "1.0.1"]

    [faker "0.2.2"]

    ; testing
    [midje "1.8.3" :scope "test"]
    [zilti/boot-midje "0.2.1-SNAPSHOT" :scope "test"]
    [http-kit.fake "0.2.1" :scope "test"]
    [ring/ring-mock "0.3.0" :scope "test"]

    [vvvvalvalval/datomock "0.1.0"]
    [philoskim/debux "0.2.0"]]

  :source-paths #{"src"})

;;; Copied from boot-test:
;;; This prevents a name collision WARNING between the test task and
;;; clojure.core/test, a function that nobody really uses or cares
;;; about.
(if ((loaded-libs) 'boot.user)
  (ns-unmap 'boot.user 'test))

(require
  '[backend.sys]
  '[system.repl :as repl :refer [system start stop go reset]]
  '[danielsz.boot-environ :refer [environ]]
  '[system.boot]
  '[zilti.boot-midje]
  '[datomic.api :as d])

(def repl-init
  '(require
     '[clojure.inspector :refer :all]
     '[datomic.api :as d]
     '[clojure.java.io :as io]
     '[clojure.string :as s]
     '[com.stuartsierra.component :as component]
     '[system.repl :as repl :refer [system]]))

(task-options!
  speak {:theme "ordinance"}

  ; This option is not used by `boot repl --server`
  ; only by the `boot repl --client`
  repl {:eval repl-init})

(alter-var-root #'midje.sweet/include-midje-checks (constantly false))

(deftask test []
  (alter-var-root #'midje.sweet/include-midje-checks (constantly true))
  (merge-env! :source-paths #{"test"})
  (zilti.boot-midje/midje))

(deftask dev
  "Run a restartable system in the Repl"
  []
  (eval repl-init)

  (comp
    (environ :env {:http-port 3002})
    (watch :verbose false)
    (system.boot/system :sys #'backend.sys/dev
                        :auto true
                        :files ["api.clj"
                                "sys.clj"
                                "handler.clj"
                                "data.clj"])

    ; (eval repl-init) above is necessary to require the convenience namespaces
    ; so we can have them when connecting from IntelliJ via lein, which can not
    ; simply use the `repl-init` defined in this build.boot
    (repl :server true)))
