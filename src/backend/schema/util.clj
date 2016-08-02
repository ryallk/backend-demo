;; Code from https://github.com/peterromfeldhk/datomic-schema
(ns backend.schema.util
  (:require [datomic.api :as d])
  (:import (clojure.lang Keyword)))

(def attr-map
  {
   ;; Required schema attributes
   :keyword         {:db/valueType :db.type/keyword}
   :string          {:db/valueType :db.type/string}
   :boolean         {:db/valueType :db.type/boolean}
   :long            {:db/valueType :db.type/long}
   :bigint          {:db/valueType :db.type/bigint}
   :float           {:db/valueType :db.type/float}
   :double          {:db/valueType :db.type/double}
   :bigdec          {:db/valueType :db.type/bigdec}
   :ref             {:db/valueType :db.type/ref}
   :instant         {:db/valueType :db.type/instant}
   :uuid            {:db/valueType :db.type/uuid}
   :uri             {:db/valueType :db.type/uri}
   :bytes           {:db/valueType :db.type/bytes}

   :one             {:db/cardinality :db.cardinality/one}
   :many            {:db/cardinality :db.cardinality/many}

   ;; Optional schema attributes
   :unique-value    {:db/unique :db.unique/value}
   :unique-identity {:db/unique :db.unique/identity}
   :index           {:db/index true}
   :fulltext        {:db/fulltext true}
   :isComponent     {:db/isComponent true}
   :noHistory       {:db/noHistory true}})

(defmulti attr-mapping class)
(defmethod attr-mapping Keyword [k] (get attr-map k))
(defmethod attr-mapping String [s] {:db/doc s})

(defn valid? [specs]
  (let [value-types #{:keyword :string :boolean :long :bigint
                      :float :double :bigdec :ref :instant :uuid :uri :bytes}
        cardinalities #{:one :many}
        [_ _ common-value-types] (clojure.data/diff (set specs) value-types)
        [_ _ common-cardinalities] (clojure.data/diff (set specs) cardinalities)]
    (cond
      (not= 1 (count common-value-types))
      (throw (ex-info "Can only have one valueType" {:use-one-of value-types
                                                     :got        common-value-types}))
      (< 1 (count common-cardinalities))
      (throw (ex-info "Cardinality can only be :one or :many" {:use-one-of cardinalities
                                                               :got        common-cardinalities}))
      :else specs)))

(defn build-part [s]
  (mapv (fn [[k v]]
          (->
            (reduce (fn [ret item]
                      (merge ret (attr-mapping item)))
                    {:db/id                 (d/tempid :db.part/db)
                     :db/ident              k
                     :db.install/_attribute :db.part/db}
                    (valid? v))
            (update :db/cardinality #(if % % :db.cardinality/one)))) s))

(defn schema [& parts]
  (reduce (fn [ret part] (into ret (build-part part))) [] parts))
