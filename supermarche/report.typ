#set document(title: "Rapport TP5 - Simulation Supermarché", author: ("Oscar", "Baptiste"))
#set page(paper: "a4", margin: 2cm)
#set text(font: "New Computer Modern", size: 11pt, lang: "fr")
#set heading(numbering: "1.1")

#page[
  #align(center + horizon)[
    #box(height: 3cm)[
      #image("Istic.png", height: 100%)
    ]

    #v(1cm)
    #text(size: 24pt, weight: "bold")[Rapport TP5 - Simulation Supermarché]

    #v(0.5cm)
    #text(size: 16pt)[Conception et Systèmes d'Exploitation]

    #v(1.5cm)
    #text(size: 14pt)[
      *Auteurs:* Oscar & Baptiste \
      #v(0.3cm)
      M1 ISTIC \
      #v(0.3cm)
      5 décembre 2025
    ]
  ]
]

#outline(title: "Table des matières", indent: auto)

#pagebreak()

= Introduction

Ce rapport présente l'implémentation d'une simulation de supermarché utilisant la programmation concurrente en Java. L'objectif est de modéliser les interactions entre différents acteurs (clients, employés) et ressources partagées (chariots, rayons, tapis de caisse).

== Contexte du projet

Dans le cadre du TP5 de Conception et Systèmes d'Exploitation, nous devons implémenter une simulation réaliste d'un supermarché où plusieurs clients effectuent leurs courses simultanément. Cette simulation met en œuvre les concepts fondamentaux de la programmation concurrente :

- *Threads* : Chaque client et employé est un thread indépendant
- *Ressources partagées* : Chariots, rayons et tapis de caisse
- *Synchronisation* : Coordination entre les différents acteurs
- *Exclusion mutuelle* : Protection des sections critiques

== Objectifs pédagogiques

+ Comprendre le fonctionnement des threads en Java
+ Maîtriser les mécanismes de synchronisation (`synchronized`, `wait()`, `notify()`)
+ Identifier et résoudre les problèmes de concurrence (deadlocks, race conditions)
+ Implémenter le pattern producteur-consommateur

= Architecture du système

== Vue d'ensemble

Le système simule un supermarché avec les éléments suivants :
- Un ensemble de *clients* qui font leurs courses
- Des *rayons* contenant différents produits
- Un *pool de chariots* en nombre limité
- Une *caisse* avec un tapis roulant (buffer)
- Des *employés* (caissier et employé de rayon)



== Les entités principales

#heading(level: 3, numbering: none)[Liste des entités]

#table(
  columns: (1fr, 2fr, 1fr),
  inset: 10pt,
  align: horizon,
  [*Entité*], [*Description*], [*Type*],
  [Client], [Acteur principal effectuant ses courses], [Thread],
  [Rayon], [Étagère contenant un type de produit], [Ressource partagée],
  [Chariots], [Pool de chariots disponibles], [Ressource limitée],
  [Tapis], [Buffer circulaire de la caisse], [Buffer borné],
  [EmpCaisse], [Employé scannant les articles], [Thread],
  [EmpRayon], [Employé réapprovisionnant les rayons], [Thread],
  [Entrepot], [Source illimitée de produits], [Ressource],
  [ProductEnum], [Énumération des types de produits], [Enum],
)

=== Client - L'acteur principal

Le client est le thread principal de la simulation. Son cycle de vie est le suivant :

+ *Entrée dans le supermarché* : Le client arrive et génère sa liste de courses aléatoire
+ *Prise d'un chariot* : Attend si aucun chariot n'est disponible (ressource limitée)
+ *Shopping* : Parcourt les rayons pour récupérer les produits de sa liste
  - Attend si un produit n'est pas disponible (rayon vide)
  - Ajoute les produits à son panier
+ *Passage en caisse* : Dépose ses articles sur le tapis
  - Attend si le tapis est plein
  - Attend que tous ses articles soient scannés
+ *Sortie* : Rend le chariot et quitte le supermarché

*Attributs principaux :*
- `clientId` : Identifiant unique du client
- `listeCourses` : Map des produits souhaités avec quantités
- `panier` : Liste des produits récupérés
- `chariots`, `rayons`, `tapis` : Références aux ressources partagées

=== Rayon - La ressource partagée

Chaque rayon contient un seul type de produit avec une capacité maximale. C'est une ressource partagée entre :
- Les *clients* qui prélèvent des produits (consommateurs)
- L'*employé de rayon* qui réapprovisionne (producteur)

*Attributs principaux :*
- `product` : Type de produit (enum)
- `nbProductMax` : Capacité maximale du rayon
- `currentAmountProducts` : Stock actuel

*Méthodes synchronisées :*
- `pickProducts(amount, clientId)` : Prélèvement atomique par un client
- `refill(amount)` : Réapprovisionnement atomique par l'employé
- `isFull()` : Vérifie si le rayon est plein

=== Chariots - Le pool de ressources limitées

Le pool de chariots représente une ressource limitée classique. Les clients doivent attendre si tous les chariots sont utilisés.

*Mécanisme de synchronisation :*
```java
public synchronized void prendreChariot(int clientId) {
    while (currentNbChariots <= 0) {
        wait(); // Attente passive
    }
    currentNbChariots--;
}

public synchronized void rendreChariot(int clientId) {
    currentNbChariots++;
    notifyAll(); // Réveille les clients en attente
}
```

=== Tapis - Le buffer borné (Pattern Producteur-Consommateur)

Le tapis de caisse est implémenté comme un *buffer circulaire borné*. C'est l'exemple classique du pattern producteur-consommateur :

- *Producteurs* : Les clients déposent leurs articles
- *Consommateur* : L'employé de caisse retire et scanne les articles

*Attributs principaux :*
- `tapis[]` : Tableau circulaire d'articles
- `max_articles` : Capacité maximale du buffer
- `start`, `end` : Pointeurs de lecture/écriture
- `size` : Nombre d'articles actuels
- `checkoutComplete` : Flag de fin de passage en caisse

*Mécanisme de synchronisation :*
- `ajouterArticle()` : Bloque si le buffer est plein (`wait()`)
- `retirerArticle()` : Bloque si le buffer est vide (`wait()`)
- Utilisation d'un *sentinel* (`-1`) pour marquer la fin des articles d'un client

=== EmpCaisse - Le consommateur

L'employé de caisse est un thread qui tourne en boucle et consomme les articles du tapis.

*Cycle de vie :*
+ Attente d'un article sur le tapis
+ Scan de l'article (simulation avec `sleep`)
+ Si sentinel (`-1`) détecté : signale la fin du passage en caisse
+ Répète jusqu'à interruption

*Statistiques collectées :*
- `totalArticlesScanned` : Total d'articles scannés
- `currentClientArticles` : Articles du client en cours

=== EmpRayon - Le réapprovisionneur

L'employé de rayon maintient les rayons approvisionnés en effectuant des allers-retours avec l'entrepôt.

*Cycle de vie :*
+ Vérifie s'il a des produits à transporter
+ Si non : va chercher des produits à l'entrepôt
+ Parcourt les rayons et réapprovisionne ceux qui ne sont pas pleins
+ Répète jusqu'à interruption

*Attributs principaux :*
- `carriedProducts` : Map des produits transportés
- `MAX_CARRY_PER_PRODUCT` : Capacité de transport par type de produit

= Mécanismes de synchronisation

== Sémaphores et Moniteurs

=== Moniteur Java (synchronized)

Java utilise le concept de *moniteur* avec le mot-clé `synchronized`. Chaque objet possède un verrou intrinsèque (intrinsic lock) qui garantit l'exclusion mutuelle.

```java
public synchronized void methode() {
    // Section critique - un seul thread à la fois
}
```

=== Gestion des chariots - Sémaphore de comptage

Les chariots fonctionnent comme un *sémaphore de comptage* :

```java
public class Chariots {
    private int currentNbChariots; // Compteur du sémaphore

    public synchronized void prendreChariot(int clientId) {
        while (currentNbChariots <= 0) {
            log("Client-" + clientId + " attend un chariot...");
            try {
                wait(); // P() - Attente si compteur = 0
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }
        }
        currentNbChariots--; // Décrémentation atomique
        log("Client-" + clientId + " prend un chariot");
    }

    public synchronized void rendreChariot(int clientId) {
        currentNbChariots++; // Incrémentation atomique
        log("Client-" + clientId + " rend son chariot");
        notifyAll(); // V() - Réveille les threads en attente
    }
}
```

=== Buffer du tapis de caisse - Producteur/Consommateur

Le tapis implémente le pattern classique avec deux conditions d'attente :

```java
public synchronized void ajouterArticle(int article, int clientId) {
    while (size >= max_articles) { // Buffer plein
        try {
            wait(); // Producteur attend
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return;
        }
    }
    tapis[end] = article;
    end = (end + 1) % max_articles;
    size++;
    notifyAll(); // Réveille le consommateur
}

public synchronized int retirerArticle() throws InterruptedException {
    while (size == 0) { // Buffer vide
        wait(); // Consommateur attend
    }
    int article = tapis[start];
    start = (start + 1) % max_articles;
    size--;
    notifyAll(); // Réveille les producteurs
    return article;
}
```

== Exclusion mutuelle

=== Accès aux rayons

Les rayons sont accédés par plusieurs threads :
- *Clients* : `pickProducts()` pour prélever
- *EmpRayon* : `refill()` pour réapprovisionner

Les deux méthodes sont `synchronized` pour garantir l'atomicité :

```java
public synchronized ProductEnum pickProducts(int amount, int clientId) {
    if (amount <= currentAmountProducts) {
        currentAmountProducts -= amount;
        log("Client-" + clientId + " prend " + amount);
        return product;
    }
    return null; // Pas assez de stock
}

public synchronized int refill(int nbproducts) {
    int spaceAvailable = nbProductMax - currentAmountProducts;
    int toAdd = Math.min(nbproducts, spaceAvailable);
    currentAmountProducts += toAdd;
    log("Réapprovisionne +" + toAdd);
    return nbproducts - toAdd; // Retourne le surplus
}
```

== Conditions d'attente

=== Attente de chariot disponible

Un client doit attendre si tous les chariots sont utilisés :

```java
while (currentNbChariots <= 0) {
    wait(); // Attente passive - libère le CPU
}
```

*Note :* L'utilisation de `while` au lieu de `if` est cruciale pour gérer les *spurious wakeups* (réveils intempestifs).

=== Attente de fin de passage en caisse

Le client attend que l'employé de caisse ait scanné tous ses articles :

```java
// Côté Client
public void waitForCheckoutComplete(int clientId) throws InterruptedException {
    while (!checkoutComplete) {
        wait();
    }
    checkoutComplete = false; // Reset pour le prochain client
}

// Côté EmpCaisse - quand il détecte le sentinel -1
if (article == -1) {
    checkoutComplete = true;
    notifyAll(); // Réveille le client
}
```

=== Attente de produit disponible

Si un rayon est vide, le client attend le réapprovisionnement :

```java
ProductEnum pickedProduct = null;
int attempts = 0;
while (pickedProduct == null) {
    pickedProduct = rayon.pickProducts(quantity, clientId);
    if (pickedProduct == null) {
        attempts++;
        if (attempts % 5 == 0) {
            log("Attend réapprovisionnement de " + produit + "...");
        }
        Thread.sleep(200); // Attente active avec pause
    }
}
```

= Problèmes rencontrés et solutions

== Deadlock sur le Tapis

*Problème :* La première version de `deposerArticles()` était entièrement `synchronized` :

```java
// VERSION BUGGUÉE
public synchronized void deposerArticles(List<Integer> articles) {
    for (int article : articles) {
        ajouterArticle(article); // Bloque si tapis plein
    }
}
```

Cela causait un *deadlock* :
+ Le client tient le verrou sur `Tapis` (via `deposerArticles`)
+ `ajouterArticle()` appelle `wait()` car le tapis est plein
+ `EmpCaisse` essaie d'appeler `retirerArticle()` mais ne peut pas acquérir le verrou
+ *Deadlock* : Le client attend de l'espace, l'employé attend le verrou

*Solution :* Retirer la synchronisation globale et ne synchroniser que les opérations atomiques :

```java
// VERSION CORRIGÉE
public void deposerArticles(List<Integer> articles, int clientId) {
    synchronized (this) {
        checkoutComplete = false; // Opération atomique courte
    }
    for (int article : articles) {
        ajouterArticle(article, clientId); // Chaque ajout gère sa propre sync
        Thread.sleep(20);
    }
    ajouterArticle(-1, clientId); // Sentinel
}
```

== ConcurrentModificationException

*Problème :* Modification de `listeCourses` pendant l'itération :

```java
// VERSION BUGGUÉE
for (Map.Entry<ProductEnum, Integer> entry : listeCourses.entrySet()) {
    acheterProduit(entry.getKey(), entry.getValue()); // Modifie listeCourses!
}
```

*Solution :* Créer une copie de la liste avant l'itération :

```java
// VERSION CORRIGÉE
List<Map.Entry<ProductEnum, Integer>> shoppingList =
    new ArrayList<>(listeCourses.entrySet());

for (Map.Entry<ProductEnum, Integer> entry : shoppingList) {
    // Maintenant on peut modifier listeCourses sans problème
    acheterProduit(entry.getKey(), entry.getValue());
}
```

== Arrêt propre des threads

*Problème :* Les threads employés ne s'arrêtaient pas correctement car l'`InterruptedException` n'était pas gérée :

```java
// VERSION BUGGUÉE
catch (InterruptedException ex) {
    // Rien - le thread continue
}
```

*Solution :* Restaurer le flag d'interruption et sortir de la boucle :

```java
// VERSION CORRIGÉE
catch (InterruptedException ex) {
    Thread.currentThread().interrupt(); // Restaure le flag
    break; // Sort de la boucle
}
```

== Race condition sur les rayons

*Problème :* Sans synchronisation, deux clients pouvaient prélever le même stock :

```java
// VERSION BUGGUÉE (non synchronized)
if (amount <= currentAmountProducts) { // Thread 1 vérifie: OK
    // Thread 2 vérifie aussi: OK (mais stock insuffisant pour les deux!)
    currentAmountProducts -= amount; // Résultat: stock négatif!
}
```

*Solution :* Utiliser `synchronized` pour rendre l'opération atomique.

= Exécution et résultats


== Exemple d'exécution

```
============================================================
        SIMULATION SUPERMARCHE DEMARREE
============================================================
[EmpRayon] Commence son service
[EmpCaisse] Pret a servir les clients
[Client-1] Entre dans le supermarche
[Client-1] Liste de courses: {BEURRE=3, LAIT=2}
[Chariots] Client-1 prend un chariot (5/6 dispo)
[Client-1] Cherche 3x BEURRE
[Rayon BEURRE] Client-1 prend 3 (reste: 2/5)
[Client-1] A pris 3x BEURRE | Panier: {BEURRE=3}
[Client-2] Entre dans le supermarche
[Client-2] Liste de courses: {FARINE=4, SUCRE=1}
[Chariots] Client-2 prend un chariot (4/6 dispo)
[Client-1] Cherche 2x LAIT
[Rayon LAIT] Client-1 prend 2 (reste: 3/5)
[Client-1] A pris 2x LAIT | Panier: {BEURRE=3, LAIT=2}
[Client-1] Se dirige vers la caisse avec 5 articles
[Tapis] Client-1 depose 5 articles
[EmpCaisse] Scanne: BEURRE (article #1)
[EmpCaisse] Scanne: BEURRE (article #2)
[EmpCaisse] Scanne: BEURRE (article #3)
[EmpCaisse] Scanne: LAIT (article #4)
[EmpCaisse] Scanne: LAIT (article #5)
[EmpCaisse] Fin client (Client-1) - 5 articles scannes
[Tapis] Client-1 a termine son passage en caisse
[Client-1] Paiement termine!
[Chariots] Client-1 rend son chariot (5/6 dispo)
[Client-1] Quitte le supermarche. Achats: {BEURRE=3, LAIT=2}
[EmpRayon] Va chercher des produits a l'entrepot
[EmpRayon] Charge: {BEURRE=5, FARINE=5, LAIT=5, SUCRE=5}
[Rayon BEURRE] Reapprovisionne +3 (stock: 5/5)
...
============================================================
        SIMULATION TERMINEE
============================================================
```

== Analyse des logs

Les logs montrent plusieurs comportements intéressants :

+ *Concurrence des clients* : Plusieurs clients font leurs courses simultanément
+ *Réapprovisionnement dynamique* : L'employé de rayon réapprovisionne quand les stocks baissent
+ *Synchronisation à la caisse* : Les clients passent un par un grâce au mécanisme de sentinel
+ *Gestion des chariots* : Les clients attendent si nécessaire

== Scénarios observés

*Scénario 1 : Attente de chariot*
```
[Chariots] Client-7 attend un chariot... (0/6 dispo)
[Chariots] Client-3 rend son chariot (1/6 dispo)
[Chariots] Client-7 prend un chariot (0/6 dispo)
```

*Scénario 2 : Attente de réapprovisionnement*
```
[Client-4] Attend reapprovisionnement de SUCRE...
[Rayon SUCRE] Reapprovisionne +5 (stock: 5/5)
[Rayon SUCRE] Client-4 prend 3 (reste: 2/5)
```

= Conclusion

Cette implémentation démontre les concepts clés de la programmation concurrente :

== Concepts maîtrisés

- *Threads et leur cycle de vie* : Création, exécution, interruption propre
- *Synchronisation avec moniteurs* : `synchronized`, `wait()`, `notify()`, `notifyAll()`
- *Pattern producteur-consommateur* : Implémentation avec buffer borné
- *Gestion des ressources partagées* : Sémaphores, exclusion mutuelle
- *Problèmes classiques* : Identification et résolution de deadlocks, race conditions

== Points d'amélioration possibles

- Utiliser `java.util.concurrent` (Semaphore, BlockingQueue) pour simplifier le code
- Ajouter plusieurs caisses avec load balancing
- Implémenter une file d'attente à la caisse
- Ajouter des statistiques de performance (temps d'attente moyen, etc.)

== Bilan

Ce TP nous a permis de comprendre en profondeur les mécanismes de synchronisation en Java et les problèmes classiques de la programmation concurrente. La simulation du supermarché est un excellent cas d'étude car elle combine plusieurs patterns (producteur-consommateur, pool de ressources, exclusion mutuelle) dans un contexte réaliste et compréhensible.
