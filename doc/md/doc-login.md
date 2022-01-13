# Documentation serveur Login / Sign in

Une fois que la connection TCP est effectuée avec le serveur, deux commandes peuvent être exécutées.

## SIGN

La commande `SIGN {username} {password}` sert à créer un nouveau compte. Seul le nom d'utilisateur et le mot de passe
sont nécessaires pour la création d'un compte.

### Succès

En cas de succès, le serveur renvoie une réponse ayant le format suivant:
```shell
OK {username} created
```

### Erreur

En cas d'erreur, le serveur renvoit une réponse commançant avec `KO` suivit d'un message d'erreur Une erreur peut être
dûe à:

* un nom d'utilisateur déjà existant
* une exception interne au serveur (bug)

### Example

<img src="../img/sign-example.png">
