(defproject
  demo-backend
  "0.1.0-SNAPSHOT"
  :dependencies
  [[org.clojure/clojure "1.8.0"]
   [org.clojure/data.csv "0.1.3"]
   [org.clojure/data.json "0.2.6"]

   [com.datomic/datomic-pro "0.9.5350"]
   ; http://docs.datomic.com/storage.html#provisioning-dynamo
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
   [jumblerg/ring.middleware.cors "1.0.1"]

   [faker "0.2.2"]

   ; testing
   [midje "1.8.3"]
   [zilti/boot-midje "0.2.1-SNAPSHOT"]
   [http-kit.fake "0.2.1"]
   [vvvvalvalval/datomock "0.1.0"]
   [philoskim/debux "0.2.0"]
   [ring/ring-mock "0.3.0"]

   ; Cursive deps
   ;[cursive/datomic-stubs "0.9.5153" :scope "provided"]
   ]
  :source-paths
  ["src" "test"]
  ; Configure ~/.lein/credentials.clj.gpg with the username and password
  ; according to https://my.datomic.com to allow IntelliJ to access datomic-pro
  :repositories {"my.datomic.com"
                 {:url   "https://my.datomic.com/repo"
                  :creds :gpg}})
