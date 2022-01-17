# Documentation serveur Menu

*Tous les examples seront fait avec un clientEntity simple* ***netcat*** *afin de pouvoir voir les discutions
clientEntity-serveur.*

Une fois entré dans le menu, vous avez deux commandes à votre disposition pour lister les jeux disponibles, ainsi que
jouer à un jeu.

## LIST

La commande `LIST` sert à connaître la liste des jeux ainsi que combien de personnes attendent d'autres connections pour
pouvoir jouer.

La réponse se présente sous la forme:

```
OK 6 chat(3/-1),tictactoe(1/2) 
```

En lisant cette réponse, nous pouvons dire:

* qu'il y a 6 personnes actuellement connectées au serveur
* que 2 jeux sont actuellements disponibles sur le serveur, ils sont sous la forme `{nomDuJeu}({nbClient}/{limite})`, nous avons donc:
  * 3 personnes connectées au chat, il n'y a pas de limite à ce jeu
  * 1 personne qui attend pour jouer au `tictactoe`, il manque une personne pour que la partie se lance

## PLAY

La commande `PLAY {jeu}` permets de jouer à un jeu. 
Si la limite du nombre de joueur est atteinte pour le jeu, alors une
nouvelle instance est créée.

La réponse se présente sous la forme:
```
OK enter in {game}
```

Regardez la documentation du jeu qui vous intéresse ici:
* [documentation du chat](doc-chat.md)
* [documentation du morpion](doc-tictactoe.md)