(ns clj-kondo.impl.types.clojure.core
  {:no-doc true})

(defmacro with-meta-fn [fn-expr]
  `(with-meta
     ~fn-expr
     {:form '~fn-expr}))

;; sorted in order of appearance in
;; https://github.com/clojure/clojure/blob/master/src/clj/clojure/core.clj

;; a lot of this work was already figured out here:
;; https://github.com/borkdude/speculative/blob/master/src/speculative/core.cljc

(def seqable->seqable {:arities {1 {:args [:seqable]
                                    :ret :seqable-out}}})

(def seqable->any {:arities {1 {:args [:seqable]
                                :ret :any}}})

(def any->boolean {:arities {1 {:args [:any]
                                :ret :boolean}}})

(def number->number {:arities {1 {:args [:number]
                                  :ret :number}}})

(def number*->number {:arities {:varargs {:args [{:op :rest :spec :number}]
                                          :ret :number}}})

(def number+->number {:arities {:varargs {:args [:number {:op :rest :spec :number}]
                                          :ret :number}}})

(def number->boolean {:arities {:varargs {:args [:number]
                                          :ret :boolean}}})

(def clojure-core
  {;; 16
   'list {:arities {:varargs {:ret :list}}}
   ;; 22
   'cons {:arities {2 {:args [:any :seqable]}}}
   ;; 49
   'first seqable->any
   ;; 57
   'next seqable->seqable
   ;; 66
   'rest seqable->seqable
   ;; 75
   'conj {:arities {0 {:args [:coll]
                       :ret :coll}
                    :varargs {:args [:coll {:op :rest :spec :any}]
                              :ret :coll}}}
   ;; 126
   'seq {:arities {1 {:args [:seqable]
                      :ret :seq}}}
   ;; 181
   'assoc {:arities {3 {:args [:nilable/associative :any :any]
                        :ret :associative}
                     :varargs {:min-arity 3
                               :args '[:nilable/associative :any :any
                                       {:op :rest
                                        :spec [:any :any]}]
                               :ret :associative}}}
   ;; 262
   'last seqable->any
   ;; 353
   'vector {:arities {:varargs {:ret :vector}}}
   ;; 367
   'vec {:arities {1 {:ret :vector}}}
   ;; 379
   'hash-map {:arities {:varargs {:args [{:op :rest :spec [:any :any]}]
                                  :ret :map}}}
   ;; 389
   'hash-set {:arities {:varargs {:args [{:op :rest :spec [:any :any]}]
                                  :ret :set}}}
   ;; 436
   'nil? any->boolean
   ;; 524
   'not any->boolean
   ;; 531
   'some? any->boolean
   ;; 544
   'str {:arities {:varargs {:args [{:op :rest
                                     :spec :any}]
                             :ret :string}}}
   ;; 648
   ;; TODO: with our current config you cannot express that the last one should be a seqable
   ;; so maybe we have to re-introduce a greedy version of star?
   'list* {:arities {:varargs {:args [{:op :rest :spec :any}]
                               :ret :list}}}
   ;; 660
   ;; TODO: with our current config you cannot express that the last one should be a seqable
   ;; so maybe we have to re-introduce a greedy version of star?
   'apply {:arities {:varargs {:args [:ifn {:op :rest :spec :any}]
                               :ret :any}}}
   ;; 718
   'concat {:arities {:varargs {:args [{:op :rest :spec :seqable}]
                                :ret :seqable}}}
   ;; 783
   '= {:arities {:varargs {:args [:any {:op :rest :spec :any}]
                           :ret :boolean}}}
   ;; 874
   'count {:arities {1 {:args [:seqable]
                        :ret :number}}}
   ;; 889
   'nth {:arities {2 {:args [:seqable :int]
                      :ret :any}
                   3 {:args [:seqable :int :any]
                      :ret :any}}}
   ;; 922
   'inc number->number
   ;; 947
   'reverse {:arities {1 {:args [:seqable]}}
             :ret :seqable-out}
   ;; 984
   '+ number*->number
   ;; 1008
   '* number*->number
   ;; 1020
   '/ number+->number
   ;; 1043
   '- number+->number
   ;; 1142
   'dec number->number
   ;; 1115
   'max number+->number
   ;; 1125
   'min number+->number
   ;; 1247
   'pos? number->boolean
   ;; 1254
   'neg? number->boolean
   ;; 1459
   'peek {:arities {1 {:args [:vector]
                       :ret :any}}}
   ;; 1467
   'pop {:arities {1 {:args [:vector]
                      :ret :vector}}}
   ;; 1504
   'dissoc {:arities {:varargs {:args [:map {:op :rest :spec :any}]
                                :ret :map}}}
   ;; 1534
   'find {:arities {2 {:args [:associative :any]}}}
   ;; 1540
   'select-keys {:arities {2 {:args [:associative :seqable]
                              :ret :map}}}
   ;; 1555
   'keys {:arities {1 {:args [:seqable]
                       :ret :seqable}}}
   ;; 1561
   'vals {:arities {1 {:args [:seqable]
                       :ret :seqable}}}
   ;; 1589
   ;;'name TODO
   ;; 2327
   'atom {:ret :atom}
   ;; 2345
   'swap! {:arities {:varargs {:args [:atom :ifn [{:op :rest
                                                   :spec :any}]]
                               :ret :any}}}
   ;; 2376
   'reset! {:arities {2 {:args [:atom :any]
                         :ret :any}}}
   ;; 2557
   'comp {:arities {:varargs [{:op :rest
                               :spec :ifn}]
                    :ret :ifn}}
   ;; 2576
   'juxt {:arities {:varargs {:args [:ifn {:op :rest
                                           :spec :ifn}]
                              :ret :ifn}}}
   ;; TODO: continue with every?, etc. in https://github.com/borkdude/speculative/blob/master/src/speculative/core.cljc
   ;; 2727
   'map {:arities {1 {:args [:ifn]
                      :ret :transducer}
                   :varargs {:args '[:ifn :seqable [{:op :rest
                                                     :spec :seqable}]]
                             :ret :seqable-out}}}
   ;; 2793
   'filter {:arities {1 {:args [:ifn]
                         :ret :transducer}
                      2 {:args [:ifn :seqable]
                         :ret :seqable-out}}}
   ;; 2826
   'remove {:arities {1 {:args [:ifn]
                         :ret :transducer}
                      2 {:args [:ifn :seqable]
                         :ret :seqable-out}}}
   ;; 4105
   'set {:ret :set}
   ;; 4981
   'subs {:arities {2 {:args [:string :nat-int]
                       :ret :string}
                    3 {:args [:string :nat-int :nat-int]
                       :ret :string}}}
   ;; 6790
   'reduce {:arities {2 {:args [:ifn :seqable]
                         :ret :any}
                      3 {:args [:ifn :any :seqable]
                         :ret :any}}}
   ;; 6887
   'into {:arities {0 {:args []
                       :ret :coll}
                    1 {:args [:coll]}
                    2 {:args [:coll :seqable]}
                    3 {:args [:coll :transducer :seqable]}}
          :fn (with-meta-fn
                (fn [args]
                  (let [t (:tag (first args))]
                    (if (identical? :any t)
                      :coll
                      t))))}
   ;; 6903
   'mapv {:arities {1 {:args [:ifn]
                       :ret :transducer}
                    :varargs {:args '[:ifn :seqable {:op :rest
                                                     :spec :seqable}]
                              :ret :vector}}}
   ;; 7313
   'filterv {:arities {2 {:args [:ifn :seqable]
                          :ret :vector}}}
   ;; 7313
   'keep {:arities {1 {:args [:ifn]
                       :ret :transducer}
                    2 {:args [:ifn :seqable]
                       :ret :seqable-out}}}})