#language: fr
@asciidoc
@order-02
Fonctionnalité: Solde Des Comptes

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

  Scénario: Solde initial
    Etant donné que CEL a créé le compte
      | IBAN                              | Client     | Libellé | Créé le               |
      | FR97 1234 5978 0100 0000 0000 134 | Christophe |         | 18/01/2025 à 12:00:00 |
    Et qu'aucun mouvement n'est constaté
    Lorsque Christophe requiert le solde du compte FR97 1234 5978 0100 0000 0000 134
    Alors le solde retourné doit être
    """json
    {
      "iban": "FR97 1234 5978 0100 0000 0000 134",
      "amount": "0,00 EUR",
      "timestamp": "2025-01-18T12:00:00Z"
    }
    """

  Scénario: Crédit de 100,00 EUR
    Etant donné que CEL a créé le compte
      | IBAN                              | Client     | Libellé | Créé le               |
      | FR97 1234 5978 0100 0000 0000 134 | Christophe |         | 18/01/2025 à 12:00:00 |
    Et que les mouvements suivants ont été constatés
      | Instant               | IBAN                              | Montant    |
      | 18/01/2025 à 12:10:00 | FR97 1234 5978 0100 0000 0000 134 | 100,00 EUR |
    Lorsque Christophe requiert le solde du compte FR97 1234 5978 0100 0000 0000 134
    Alors le solde retourné doit être
    """json
    {
      "iban": "FR97 1234 5978 0100 0000 0000 134",
      "amount": "100,00 EUR",
      "timestamp": "2025-01-18T12:10:00Z"
    }
    """

  Scénario: Crédit de 100,00 USD

  Un mouvement peut s'appliquer sur une devise différente de celle du compte.
  Dans ce cas, une conversion peut s'appliquer.

  Ici, nous avons choisi d'appliquer une conversion USD/EUR égale à 0.8.

    Etant donné que CEL a créé le compte
      | IBAN                              | Client     | Libellé | Créé le               |
      | FR97 1234 5978 0100 0000 0000 134 | Christophe |         | 18/01/2025 à 12:00:00 |
    Et que les mouvements suivants ont été constatés
      | Instant               | IBAN                              | Montant    |
      | 18/01/2025 à 12:10:00 | FR97 1234 5978 0100 0000 0000 134 | 100,00 USD |
    Lorsque Christophe requiert le solde du compte FR97 1234 5978 0100 0000 0000 134
    Alors le solde retourné doit être
    """json
    {
      "iban": "FR97 1234 5978 0100 0000 0000 134",
      "amount": "80,00 EUR",
      "timestamp": "2025-01-18T12:10:00Z"
    }
    """

  Scénario: Crédit de 100,00 EUR puis débit de 50,00 EUR
    Etant donné que CEL a créé le compte
      | IBAN                              | Client     | Libellé | Créé le               |
      | FR97 1234 5978 0100 0000 0000 134 | Christophe |         | 18/01/2025 à 12:00:00 |
    Et que les mouvements suivants ont été constatés
      | Instant               | IBAN                              | Montant    |
      | 18/01/2025 à 12:10:00 | FR97 1234 5978 0100 0000 0000 134 | 100,00 EUR |
      | 18/01/2025 à 12:50:00 | FR97 1234 5978 0100 0000 0000 134 | -50,00 EUR |
    Lorsque Christophe requiert le solde du compte FR97 1234 5978 0100 0000 0000 134
    Alors le solde retourné doit être
    """json
    {
      "iban": "FR97 1234 5978 0100 0000 0000 134",
      "amount": "50,00 EUR",
      "timestamp": "2025-01-18T12:50:00Z"
    }
    """

  Scénario: Débit de 100,00 EUR
    Etant donné que CEL a créé le compte
      | IBAN                              | Client     | Libellé | Créé le               |
      | FR97 1234 5978 0100 0000 0000 134 | Christophe |         | 18/01/2025 à 12:00:00 |
    Et que les mouvements suivants ont été constatés
      | Instant               | IBAN                              | Montant     |
      | 18/01/2025 à 12:10:00 | FR97 1234 5978 0100 0000 0000 134 | -100,00 EUR |
    Lorsque Christophe requiert le solde du compte FR97 1234 5978 0100 0000 0000 134
    Alors le solde retourné doit être
    """json
    {
      "iban": "FR97 1234 5978 0100 0000 0000 134",
      "amount": "0,00 EUR",
      "timestamp": "2025-01-18T12:00:00Z"
    }
    """

  Scénario: Crédit de 100,00 EUR sur plusieurs comptes
    Etant donné que CEL a créé les comptes
      | IBAN                              | Client     | Libellé  | Créé le               |
      | FR97 1234 5978 0100 0000 0000 134 | Christophe | Perso    | 18/01/2025 à 12:00:00 |
      | FR53 1234 5978 0200 0000 0000 135 | Christophe | Tirelire | 18/01/2025 à 12:00:00 |
    Et que les mouvements suivants ont été constatés
      | Instant               | IBAN                              | Montant    |
      | 18/01/2025 à 12:10:00 | FR97 1234 5978 0100 0000 0000 134 | 100,00 EUR |
      | 18/01/2025 à 12:30:00 | FR53 1234 5978 0200 0000 0000 135 | 100,00 EUR |
    Lorsque Christophe requiert le solde du compte FR97 1234 5978 0100 0000 0000 134
    Alors le solde retourné doit être
    """json
    {
      "iban": "FR97 1234 5978 0100 0000 0000 134",
      "amount": "100,00 EUR",
      "timestamp": "2025-01-18T12:10:00Z"
    }
    """

  Scénario: Crédit de 100,00 EUR sur les comptes de différents clients
    Etant donné que CEL a créé les comptes
      | IBAN                              | Client     | Libellé | Créé le               |
      | FR97 1234 5978 0100 0000 0000 134 | Christophe | Perso   | 18/01/2025 à 12:00:00 |
      | FR38 1234 5978 0100 0000 0000 226 | Caroline   |         | 18/01/2025 à 12:00:00 |
    Et que les mouvements suivants ont été constatés
      | Instant               | IBAN                              | Montant    |
      | 18/01/2025 à 12:10:00 | FR97 1234 5978 0100 0000 0000 134 | 100,00 EUR |
      | 18/01/2025 à 12:30:00 | FR38 1234 5978 0100 0000 0000 226 | 100,00 EUR |
    Lorsque Christophe requiert le solde du compte FR97 1234 5978 0100 0000 0000 134
    Alors le solde retourné doit être
    """json
    {
      "iban": "FR97 1234 5978 0100 0000 0000 134",
      "amount": "100,00 EUR",
      "timestamp": "2025-01-18T12:10:00Z"
    }
    """
