#language: fr
@asciidoc
@order-03
Fonctionnalité: Mouvements Sur Les Comptes

  Les applications mobile et web s'adressent au service pour récupérer le solde du compte d'un client.
  Notons que les contrôles de sécurité ont déjà été effectués en amont à la connexion du client dans son espace sécurisé.

  Contexte: Les clients du CEL

  Prenons ici l'exemple pour un client du CEL.
  Il s'est identifié auprès du CEL, nous avons donc accès à ses informations d'identification.

  Nous prendrons le parti de référencer les clients par leur prénom, qui commencera par un `C` comme client...

    Etant donné nos clients
      | Identifiant                          | Nom de famille | Prénom     | E-mail                                 |
      | 3c128dc9-8166-4b30-b821-59a02708f3cd | LeClient       | Christophe | christophe.leclient@un-fournisseur.com |
      | be63d4ac-f06e-4fef-a0e9-2248687a0724 | LaCliente      | Caroline   | caroline.lacliente@une-societe.com     |

  Scénario: Aucun mouvement
    Etant donné que CEL a créé le compte
      | IBAN                              | Client     | Libellé | Créé le               |
      | FR97 1234 5978 0100 0000 0000 134 | Christophe |         | 18/01/2025 à 12:00:00 |
    Et qu'aucun mouvement n'est constaté
    Lorsque Christophe requiert la liste de mouvements sur le compte FR97 1234 5978 0100 0000 0000 134
    Alors la liste de mouvements retournée doit être
    """json
    {
      "iban": "FR97 1234 5978 0100 0000 0000 134",
      "movements": []
    }
    """

  Scénario: Crédit de EUR 100.00
    Etant donné que CEL a créé le compte
      | IBAN                              | Client     | Libellé | Créé le               |
      | FR97 1234 5978 0100 0000 0000 134 | Christophe |         | 18/01/2025 à 12:00:00 |
    Et que les mouvements suivants ont été constatés
      | Instant               | IBAN                              | Montant    |
      | 18/01/2025 à 12:10:00 | FR97 1234 5978 0100 0000 0000 134 | EUR 100.00 |
    Lorsque Christophe requiert la liste de mouvements sur le compte FR97 1234 5978 0100 0000 0000 134
    Alors la liste de mouvements retournée doit être
    """json
    {
      "iban": "FR97 1234 5978 0100 0000 0000 134",
      "movements": [
        {
          "amount": "100,00 EUR",
          "timestamp": "2025-01-18T12:10:00Z"
        }
      ]
    }
    """

  Scénario: Crédit de USD 100.00
    Etant donné que CEL a créé le compte
      | IBAN                              | Client     | Libellé | Créé le               |
      | FR97 1234 5978 0100 0000 0000 134 | Christophe |         | 18/01/2025 à 12:00:00 |
    Et que les mouvements suivants ont été constatés
      | Instant               | IBAN                              | Montant    |
      | 18/01/2025 à 12:10:00 | FR97 1234 5978 0100 0000 0000 134 | USD 100.00 |
    Lorsque Christophe requiert la liste de mouvements sur le compte FR97 1234 5978 0100 0000 0000 134
    Alors la liste de mouvements retournée doit être
    """json
    {
      "iban": "FR97 1234 5978 0100 0000 0000 134",
      "movements": [
        {
          "amount": "100,00 USD",
          "timestamp": "2025-01-18T12:10:00Z"
        }
      ]
    }
    """

  Scénario: Crédit de EUR 100.00 puis débit de EUR 50.00
    Etant donné que CEL a créé le compte
      | IBAN                              | Client     | Libellé | Créé le               |
      | FR97 1234 5978 0100 0000 0000 134 | Christophe |         | 18/01/2025 à 12:00:00 |
    Et que les mouvements suivants ont été constatés
      | Instant               | IBAN                              | Montant    |
      | 18/01/2025 à 12:10:00 | FR97 1234 5978 0100 0000 0000 134 | EUR 100.00 |
      | 18/01/2025 à 12:30:00 | FR97 1234 5978 0100 0000 0000 134 | EUR -50.00 |
    Lorsque Christophe requiert la liste de mouvements sur le compte FR97 1234 5978 0100 0000 0000 134
    Alors la liste de mouvements retournée doit être
    """json
    {
      "iban": "FR97 1234 5978 0100 0000 0000 134",
      "movements": [
        {
          "amount": "-50,00 EUR",
          "timestamp": "2025-01-18T12:30:00Z"
        },
        {
          "amount": "100,00 EUR",
          "timestamp": "2025-01-18T12:10:00Z"
        }
      ]
    }
    """

  Scénario: Débit de EUR 100.00
    Etant donné que CEL a créé le compte
      | IBAN                              | Client     | Libellé | Créé le               |
      | FR97 1234 5978 0100 0000 0000 134 | Christophe |         | 18/01/2025 à 12:00:00 |
    Et que les mouvements suivants ont été constatés
      | Instant               | IBAN                              | Montant     |
      | 18/01/2025 à 12:10:00 | FR97 1234 5978 0100 0000 0000 134 | EUR -100.00 |
    Lorsque Christophe requiert la liste de mouvements sur le compte FR97 1234 5978 0100 0000 0000 134
    Alors la liste de mouvements retournée doit être
    """json
    {
      "iban": "FR97 1234 5978 0100 0000 0000 134",
      "movements": []
    }
    """

  Scénario: Crédit de EUR 100.00 sur plusieurs comptes
    Etant donné que CEL a créé les comptes
      | IBAN                              | Client     | Libellé  | Créé le               |
      | FR97 1234 5978 0100 0000 0000 134 | Christophe | Perso    | 18/01/2025 à 12:00:00 |
      | FR53 1234 5978 0200 0000 0000 135 | Christophe | Tirelire | 18/01/2025 à 12:00:00 |
    Et que les mouvements suivants ont été constatés
      | Instant               | IBAN                              | Montant    |
      | 18/01/2025 à 12:10:00 | FR97 1234 5978 0100 0000 0000 134 | EUR 100.00 |
      | 18/01/2025 à 12:30:00 | FR53 1234 5978 0200 0000 0000 135 | EUR 100.00 |
    Lorsque Christophe requiert la liste de mouvements sur le compte FR97 1234 5978 0100 0000 0000 134
    Alors la liste de mouvements retournée doit être
    """json
    {
      "iban": "FR97 1234 5978 0100 0000 0000 134",
      "movements": [
        {
          "amount": "100,00 EUR",
          "timestamp": "2025-01-18T12:10:00Z"
        }
      ]
    }
    """

  Scénario: Crédit de EUR 100.00 sur les comptes de différents clients
    Etant donné que CEL a créé les comptes
      | IBAN                              | Client     | Libellé | Créé le               |
      | FR97 1234 5978 0100 0000 0000 134 | Christophe | Perso   | 18/01/2025 à 12:00:00 |
      | FR38 1234 5978 0100 0000 0000 226 | Caroline   |         | 18/01/2025 à 12:00:00 |
    Et que les mouvements suivants ont été constatés
      | Instant               | IBAN                              | Montant    |
      | 18/01/2025 à 12:10:00 | FR97 1234 5978 0100 0000 0000 134 | EUR 100.00 |
      | 18/01/2025 à 12:30:00 | FR38 1234 5978 0100 0000 0000 226 | EUR 100.00 |
    Lorsque Christophe requiert la liste de mouvements sur le compte FR97 1234 5978 0100 0000 0000 134
    Alors la liste de mouvements retournée doit être
    """json
    {
      "iban": "FR97 1234 5978 0100 0000 0000 134",
      "movements": [
        {
          "amount": "100,00 EUR",
          "timestamp": "2025-01-18T12:10:00Z"
        }
      ]
    }
    """
