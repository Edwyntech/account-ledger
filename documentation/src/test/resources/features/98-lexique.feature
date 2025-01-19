#language: fr
@asciidoc
@order-98
@glossary
Fonctionnalité: Lexique

  [glossary]

  [[client, Client]] Client (anglais: _Customer_) :: Personne physique ou morale inscrite au CEL.
  +
  Un client est identifié par :

  * Un identifiant unique, généré par la banque
  * Son nom de famille
  * Son prénom
  * Son email

  [[compte, Compte]] Compte (anglais: _Account_) :: Un <<compte,compte>> en banque, soit courant, soit d'épargne, géré par le CEL.
  +
  Un <<compte,compte>> CEL possède donc :

  * Son <<iban,IBAN>>.
  * L'identifiant du client qui détient ce <<compte,compte>>.
  * Un libellé donné par le client à sa création, par défaut "Compte courant" ou "Compte d'épargne" selon son type.
  * Sa date de création.
  +
  [IMPORTANT]
  ====
  Par défaut, les libellés des comptes seront

  * _"Compte courant"_ pour un compte courant.
  * _"Compte d'épargne"_ pour un compte d'épargne.
  ====
  +
  Un <<iban,IBAN>> CEL sera toujours de la forme FR[II] 1234 5[DDD] [TT][CC CCCC CCCC C][NN] où :

  * FR est le code pays de la banque, donc la France
  * `II` est un checksum à deux chiffres sur l'entièreté de l'<<iban,IBAN>>
  * `1234 5` identifie la banque CEL
  * `DDD` correspond à la devise du <<compte,compte>>, son code est décrit par l'<<iso-4217,ISO 4217>>
  * `TT` correspond au type de <<compte,compte>>, courant ou d'épargne
  * `CC CCCC CCCC C` est le numéro de ce <<compte,compte>>
  * `NN` est un checksum à deux chiffres dit _national_
  +
  Les comptes du CEL sont de deux types :

  * Les <<compte,comptes>> courants dont le code est `01`.
  * Les <<compte,comptes>> d'épargne, dont le code est `02`.
  +
  [TIP]
  ====
  Par exemple

  * FR97 1234 5978 0100 0000 0000 134 identifie un <<compte,compte>> courant
  * FR53 1234 5978 0200 0000 0000 135 identifie un <<compte,compte>> d'épargne
  ====

  [[mouvement, Mouvement]] Mouvement (anglais: _Movement_) :: Opération de débit ou de crédit sur le solde d'un compte.
  Ces opérations sont générées par des paiements ou des virements bancaires.
  +
  Un <<mouvement,mouvement>> bancaire contient :

  * l'<<iban,IBAN>> du <<compte,compte>> observé
  * L'instant de l'opération
  * Le montant de l'opération

  [[solde, Solde]] Solde (anglais: _Balance_) :: Montant des avoirs d'un compte.
  Cette valeur existe à un instant donné et s'exprime comme un montant dans la devise du compte.
  +
  Les <<solde,soldes>> ont leur propre modèle qui se compose ainsi :

  * L'instant de calcul
  * L'<<iban,IBAN>> du <<compte,compte>> référent
  * Son montant, exprimé dans la devise du <<compte,compte>> associé
  +
  IMPORTANT: Le <<solde,solde>> initial d'un <<compte,compte>> est toujours de zéro.
  +
  IMPORTANT: Aucun découvert n'est autorisé, par conséquent tous les soldes doivent avoir un montant positif.

  Scénario: Root
