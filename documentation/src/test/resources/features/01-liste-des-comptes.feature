#language: fr
@asciidoc
@order-01
Fonctionnalité: Liste Des Comptes

  Les applications mobile et web s'adressent au service pour récupérer la liste des comptes d'un client.
  Notons que les contrôles de sécurité ont déjà été effectués en amont à la connexion du client dans son espace sécurisé.

  Contexte: Les clients du CEL

  Prenons ici l'exemple pour un client du CEL.
  Il s'est inscrit au CEL, nous avons donc accès à ses informations d'identification.

  Nous prendrons le parti de référencer les clients par leur prénom, qui commencera par un `C` comme client...

    Etant donné nos clients
      | Identifiant                          | Nom de famille | Prénom     | E-mail                                 |
      | 3c128dc9-8166-4b30-b821-59a02708f3cd | LeClient       | Christophe | christophe.leclient@un-fournisseur.com |
      | be63d4ac-f06e-4fef-a0e9-2248687a0724 | LaCliente      | Caroline   | caroline.lacliente@une-societe.com     |

  Scénario: Un client sans compte
    Etant donné que CEL n'a créé aucun compte
    Lorsque Christophe requiert la liste de ses comptes
    Alors la liste de comptes retournée doit être
    """json
    {
      "customerId": "3c128dc9-8166-4b30-b821-59a02708f3cd",
      "accounts": []
    }
    """

  Scénario: Un seul compte
    Etant donné que CEL a créé le compte
      | IBAN                              | Client     | Libellé | Créé le               |
      | FR97 1234 5978 0100 0000 0000 134 | Christophe |         | 18/01/2025 à 12:00:00 |
    Lorsque Christophe requiert la liste de ses comptes
    Alors la liste de comptes retournée doit être
    """json
    {
      "customerId": "3c128dc9-8166-4b30-b821-59a02708f3cd",
      "accounts": [
        {
          "iban": "FR97 1234 5978 0100 0000 0000 134",
          "label": "Compte courant"
        }
      ]
    }
    """

  Scénario: Un compte de chaque type
    Etant donné que CEL a créé les comptes
      | IBAN                              | Client     | Libellé  | Créé le               |
      | FR97 1234 5978 0100 0000 0000 134 | Christophe | Perso    | 18/01/2025 à 12:00:00 |
      | FR53 1234 5978 0200 0000 0000 135 | Christophe | Tirelire | 18/01/2025 à 12:00:00 |
    Lorsque Christophe requiert la liste de ses comptes
    Alors la liste de comptes retournée doit être
    """json
    {
      "customerId": "3c128dc9-8166-4b30-b821-59a02708f3cd",
      "accounts": [
        {
          "iban": "FR97 1234 5978 0100 0000 0000 134",
          "label": "Perso"
        },
        {
          "iban": "FR53 1234 5978 0200 0000 0000 135",
          "label": "Tirelire"
        }
      ]
    }
    """

  Scénario: Plusieurs clients, chacun un compte
    Etant donné que CEL a créé le compte
      | IBAN                              | Client     | Libellé | Créé le               |
      | FR97 1234 5978 0100 0000 0000 134 | Christophe |         | 18/01/2025 à 12:00:00 |
      | FR38 1234 5978 0100 0000 0000 226 | Caroline   |         | 18/01/2025 à 12:00:00 |
    Lorsque Caroline requiert la liste de ses comptes
    Alors la liste de comptes retournée doit être
    """json
    {
      "customerId": "be63d4ac-f06e-4fef-a0e9-2248687a0724",
      "accounts": [
        {
          "iban": "FR38 1234 5978 0100 0000 0000 226",
          "label": "Compte courant"
        }
      ]
    }
    """
