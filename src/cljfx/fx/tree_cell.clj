(ns cljfx.fx.tree-cell
  (:require [cljfx.lifecycle.composite :as lifecycle.composite]
            [cljfx.lifecycle :as lifecycle]
            [cljfx.fx.indexed-cell :as fx.indexed-cell]
            [cljfx.coerce :as coerce])
  (:import [javafx.scene.control TreeCell]
           [javafx.scene AccessibleRole]))

(set! *warn-on-reflection* true)

(def lifecycle
  (lifecycle.composite/describe TreeCell
    :ctor []
    :extends [fx.indexed-cell/lifecycle]
    :props {;; overrides
            :style-class [:list lifecycle/scalar :coerce coerce/style-class
                          :default ["cell" "indexed-cell" "tree-cell"]]
            :accessible-role [:setter lifecycle/scalar :coerce (coerce/enum AccessibleRole)
                              :default :tree-item]
            ;; definitions
            :disclosure-node [:setter lifecycle/dynamic]}))

(defn create [f]
  (let [*props (volatile! {})]
    (proxy [TreeCell] []
      (updateItem [item empty]
        (let [^TreeCell this this
              props @*props]
          (proxy-super updateItem item empty)
          (vreset! *props (f props this item empty)))))))