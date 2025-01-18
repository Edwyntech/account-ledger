#language: fr
@asciidoc
@order-02
@WIP
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
    Etant donné que Christophe détient le compte
      | IBAN                              | Libellé | Créé le               |
      | FR97 1234 5978 0100 0000 0000 134 |         | 18/01/2025 à 12:00:00 |
    Et qu'aucun mouvement n'est constaté le 18/01/2025 à 13:00:00
    Lorsque Christophe requiert le solde du compte FR97 1234 5978 0100 0000 0000 134
    Alors le solde retourné doit être
      | Instant               | IBAN                              | Montant  |
      | 18/01/2025 à 13:00:00 | FR97 1234 5978 0100 0000 0000 134 | EUR 0.00 |

  Scénario: Crédit de EUR 100.00
    Etant donné que Christophe détient le compte
      | IBAN                              | Libellé | Créé le               |
      | FR97 1234 5978 0100 0000 0000 134 |         | 18/01/2025 à 12:00:00 |
    Et que les mouvements suivants ont été constatés le 18/01/2025 à 13:00:00
      | Instant               | IBAN                              | Montant    |
      | 18/01/2025 à 12:10:00 | FR97 1234 5978 0100 0000 0000 134 | EUR 100.00 |
    Lorsque Christophe requiert le solde du compte FR97 1234 5978 0100 0000 0000 134
    Alors le solde retourné doit être
      | Instant               | IBAN                              | Montant    |
      | 18/01/2025 à 13:00:00 | FR97 1234 5978 0100 0000 0000 134 | EUR 100.00 |

  Scénario: Crédit de USD 100.00

  Un mouvement peut s'appliquer sur une devise différente de celle du compte.
  Dans ce cas, une conversion peut s'appliquer.

  Ici, nous avons choisi d'appliquer une conversion USD/EUR égale à 0.8.

    Etant donné que Christophe détient le compte
      | IBAN                              | Libellé | Créé le               |
      | FR97 1234 5978 0100 0000 0000 134 |         | 18/01/2025 à 12:00:00 |
    Et que les mouvements suivants ont été constatés le 18/01/2025 à 13:00:00
      | Instant               | IBAN                              | Montant    |
      | 18/01/2025 à 12:10:00 | FR97 1234 5978 0100 0000 0000 134 | USD 100.00 |
    Lorsque Christophe requiert le solde du compte FR97 1234 5978 0100 0000 0000 134
    Alors le solde retourné doit être
      | Instant               | IBAN                              | Montant   |
      | 18/01/2025 à 13:00:00 | FR97 1234 5978 0100 0000 0000 134 | EUR 80.00 |

  Scénario: Crédit de EUR 100.00 puis débit de EUR 50.00
    Etant donné que Christophe détient le compte
      | IBAN                              | Libellé | Créé le               |
      | FR97 1234 5978 0100 0000 0000 134 |         | 18/01/2025 à 12:00:00 |
    Et que les mouvements suivants ont été constatés le 18/01/2025 à 13:00:00
      | Instant               | IBAN                              | Montant    |
      | 18/01/2025 à 12:10:00 | FR97 1234 5978 0100 0000 0000 134 | EUR 100.00 |
      | 18/01/2025 à 12:50:00 | FR97 1234 5978 0100 0000 0000 134 | EUR -50.00 |
    Lorsque Christophe requiert le solde du compte FR97 1234 5978 0100 0000 0000 134
    Alors le solde retourné doit être
      | Instant               | IBAN                              | Montant   |
      | 18/01/2025 à 13:00:00 | FR97 1234 5978 0100 0000 0000 134 | EUR 50.00 |

  Scénario: Débit de EUR 100.00
    Etant donné que Christophe détient le compte
      | IBAN                              | Libellé | Créé le               |
      | FR97 1234 5978 0100 0000 0000 134 |         | 18/01/2025 à 12:00:00 |
    Et que les mouvements suivants ont été constatés le 18/01/2025 à 13:00:00
      | Instant               | IBAN                              | Montant     |
      | 18/01/2025 à 12:10:00 | FR97 1234 5978 0100 0000 0000 134 | EUR -100.00 |
    Lorsque Christophe requiert le solde du compte FR97 1234 5978 0100 0000 0000 134
    Alors le solde retourné doit être
      | Instant               | IBAN                              | Montant  |
      | 18/01/2025 à 13:00:00 | FR97 1234 5978 0100 0000 0000 134 | EUR 0.00 |

  Scénario: Crédit de EUR 100.00 sur plusieurs comptes
    Etant donné que Christophe détient les comptes
      | IBAN                              | Libellé  | Créé le               |
      | FR97 1234 5978 0100 0000 0000 134 | Perso    | 18/01/2025 à 12:00:00 |
      | FR53 1234 5978 0200 0000 0000 135 | Tirelire | 18/01/2025 à 12:00:00 |
    Et que les mouvements suivants ont été constatés le 18/01/2025 à 13:00:00
      | Instant               | IBAN                              | Montant    |
      | 18/01/2025 à 12:10:00 | FR97 1234 5978 0100 0000 0000 134 | EUR 100.00 |
      | 18/01/2025 à 12:30:00 | FR53 1234 5978 0200 0000 0000 135 | EUR 100.00 |
    Lorsque Christophe requiert le solde du compte FR97 1234 5978 0100 0000 0000 134
    Alors le solde retourné doit être
      | Instant               | IBAN                              | Montant    |
      | 18/01/2025 à 13:00:00 | FR97 1234 5978 0100 0000 0000 134 | EUR 100.00 |

  Scénario: Crédit de EUR 100.00 sur les comptes de différents clients
    Etant donné que Christophe détient le compte
      | IBAN                              | Libellé | Créé le               |
      | FR97 1234 5978 0100 0000 0000 134 | Perso   | 18/01/2025 à 12:00:00 |
    Et que Caroline détient le compte
      | IBAN                              | Libellé | Créé le               |
      | FR38 1234 5978 0100 0000 0000 226 |         | 18/01/2025 à 12:00:00 |
    Et que les mouvements suivants ont été constatés le 18/01/2025 à 13:00:00
      | Instant               | IBAN                              | Montant    |
      | 18/01/2025 à 12:10:00 | FR97 1234 5978 0100 0000 0000 134 | EUR 100.00 |
      | 18/01/2025 à 12:30:00 | FR38 1234 5978 0100 0000 0000 226 | EUR 100.00 |
    Lorsque Christophe requiert le solde du compte FR97 1234 5978 0100 0000 0000 134
    Alors le solde retourné doit être
      | Instant               | IBAN                              | Montant    |
      | 18/01/2025 à 13:00:00 | FR97 1234 5978 0100 0000 0000 134 | EUR 100.00 |
