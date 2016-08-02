(ns backend.api
  (:require
    [backend.search :as search]
    [backend.manage :as manage]

    [system.repl :refer [system]]
    [castra.core :refer [defrpc]]))

(defrpc search-contacts [text]
        (search/search system text))

(defrpc create [contact]
        (manage/create system contact))

(defrpc delete [contact-id]
        (manage/delete system contact-id))
