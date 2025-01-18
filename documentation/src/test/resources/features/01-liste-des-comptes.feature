#language: fr
@asciidoc
@order-01
@WIP
Fonctionnalité: Liste Des Comptes

  Les applications mobile et web s'adressent au service pour récupérer la liste des comptes d'un client.
  Notons que les contrôles de sécurité ont déjà été effectués en amont à la connexion du client dans son espace sécurisé.

  Contexte: Les clients du CEL

  Prenons ici l'exemple pour un client du CEL.
  Il s'est identifié auprès du CEL, nous avons donc accès à ses informations d'identification.

  Nous prendrons le parti de référencer les clients par leur prénom, qui commencera par un `C` comme client...

    Etant donné nos clients
      | Identifiant                          | Nom de famille | Prénom     | E-mail                                 |
      | 3c128dc9-8166-4b30-b821-59a02708f3cd | LeClient       | Christophe | christophe.leclient@un-fournisseur.com |
      | be63d4ac-f06e-4fef-a0e9-2248687a0724 | LaCliente      | Caroline   | caroline.lacliente@une-societe.com     |

  Scénario: Un seul compte
    Etant donné que Christophe détient le compte
      | IBAN                              | Libellé | Créé le               |
      | FR97 1234 5978 0100 0000 0000 134 |         | 18/01/2025 à 12:00:00 |
    Lorsque Christophe requiert la liste de ses comptes
    Alors la liste des comptes retournée doit être
      | IBAN                              | Libellé        | Créé le               |
      | FR97 1234 5978 0100 0000 0000 134 | Compte courant | 18/01/2025 à 12:00:00 |

  Scénario: Un compte de chaque type
    Etant donné que Christophe détient les comptes
      | IBAN                              | Libellé  | Créé le               |
      | FR97 1234 5978 0100 0000 0000 134 | Perso    | 18/01/2025 à 12:00:00 |
      | FR53 1234 5978 0200 0000 0000 135 | Tirelire | 18/01/2025 à 12:00:00 |
    Lorsque Christophe requiert la liste de ses comptes
    Alors la liste des comptes retournée doit être
      | IBAN                              | Libellé  | Créé le               |
      | FR97 1234 5978 0100 0000 0000 134 | Perso    | 18/01/2025 à 12:00:00 |
      | FR53 1234 5978 0200 0000 0000 135 | Tirelire | 18/01/2025 à 12:00:00 |

  Scénario: Plusieurs clients, chacun un compte
    Etant donné que Christophe détient le compte
      | IBAN                              | Libellé | Créé le               |
      | FR97 1234 5978 0100 0000 0000 134 |         | 18/01/2025 à 12:00:00 |
    Et que Caroline détient le compte
      | IBAN                              | Libellé | Créé le               |
      | FR38 1234 5978 0100 0000 0000 226 |         | 18/01/2025 à 12:00:00 |
    Lorsque Caroline requiert la liste de ses comptes
    Alors la liste des comptes retournée doit être
      | IBAN                              | Libellé        | Créé le               |
      | FR38 1234 5978 0100 0000 0000 226 | Compte courant | 18/01/2025 à 12:00:00 |

  Scénario: Un client sans compte
    Etant donné que Christophe ne détient aucun compte
    Lorsque Christophe requiert la liste de ses comptes
    Alors le message d'erreur renvoyé doit être
      """
      Le client 3c128dc9-8166-4b30-b821-59a02708f3cd ne possède aucun compte
      """
