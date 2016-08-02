(ns backend.schema.contact
  (:require
    [datomic.api]
    [backend.schema.util :as util]))

(def contact
  {:contact/name        [:string :fulltext "Name of the contact"]
   :contact/location    [:string "Location of contact"]
   :contact/description [:string :fulltext "Description of contact"]
   :contact/company     [:string "Company where contact works"]})

(def contact-schema
  (util/schema contact))
