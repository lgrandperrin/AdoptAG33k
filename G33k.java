/**
 * Adopte un g33k - CYNTHIA MAILLARD - LOIC GRANDPERRIN
 */
 
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Scanner;

public class G33k{

	static void RechercherG33k(int connexion, int search, String recherche) { //Recherche un utilisateur suivant le type de recherche choisis par l'utilisateur
		String sql = "";

		switch(search){
			case 1 : //par genre
				sql = "SELECT profil.*, interet_utilisateur.iuInteret FROM profil INNER JOIN interet_utilisateur ON prUtilisateur = iuUtilisateur WHERE prSexe ='"+recherche+"'";
			break;
			case 2 : //par couleur de cheveux
				sql = "SELECT profil.*, interet_utilisateur.iuInteret FROM profil INNER JOIN interet_utilisateur ON prUtilisateur = iuUtilisateur WHERE prCheveux ='"+recherche+"'";
			break;
			case 3 : //par interet
				sql = "SELECT profil.*, interet_utilisateur.iuInteret FROM profil INNER JOIN interet_utilisateur ON prUtilisateur = iuUtilisateur WHERE iuInteret ='"+recherche+"'";
			break;
		}
		int res = BD.executerSelect(connexion, sql);
			if (res >= 0){
				while (BD.suivant(res)) {//Affichage des utilisateurs
					String prUtilisateur = BD.attributString(res, "prUtilisateur");
					String prSexe = BD.attributString(res, "prSexe");
					int prTaille = BD.attributInt(res, "prTaille");
					String prCheveux = BD.attributString(res, "prCheveux");
					int prPoids = BD.attributInt(res, "prPoids");
					String prBio = BD.attributString(res, "prBio");
					String iuInteret = BD.attributString(res, "iuInteret");
					Ecran.afficherln("Login : ", prUtilisateur);
					Ecran.afficherln("Sexe : ", prSexe);
					Ecran.afficherln("Taille (cm) : ",prTaille);
					Ecran.afficherln("Cheveux : ", prCheveux);
					Ecran.afficherln("Poids (kg) : ",prPoids);
					Ecran.afficherln("Biographie : ", prBio);
					Ecran.afficherln("Interet : ", iuInteret);
					Ecran.afficherln("-----------------------");
				}
			}
			else{//Si aucune personne ne rentre dans les criteres
				Ecran.afficherln("Aucune personne correspond a ce critere.");
			}
	}

  
	public static void main(String[] args) {

		int connexion = BD.ouvrirConnexion("127.0.0.1","adopte1g33k","root","");
		
		int disconnected = -2; //Variable de choix de menu si utilisateur deconnecter
		int connected = -1; //Variable de choix de menu si utilisateur connecter
		
		while (disconnected != -1 ) { //Pour gerer la fermeture de la fenetre interface
			Ecran.afficherln("Espace Membre");
			Ecran.afficherln(" 1 - Inscription");      //Inscription a la base de donnee
			Ecran.afficherln(" 2 - Connexion");      // Connexion a la base de donnee
			Ecran.afficherln(" 0 - Quitter le programme");
			disconnected = Clavier.saisirInt();
			
			if(disconnected < 0 || disconnected > 2){
				Ecran.afficherln("Choix incorrect: Resaisissez le numero !"); //Message d'erreur si choix incorrect
				disconnected = -2; //Remise a une valeur differente de -1 
			}
			
			switch (disconnected) {
				case 1 ://Formulaire d'inscription puis retour au menu Espace Membre
					Ecran.afficherln("Ce site est reserve exclusivement aux g33ks majeurs (18+)");
					Ecran.afficher("Saisir votre login : ");
					String usLogin = Clavier.saisirString();
					Ecran.afficher("Saisir votre mot de passe : ");
					String usPassword = Clavier.saisirString();
					String usRole = "aucun";
					String sql = "INSERT INTO utilisateur(usLogin,usPassword,usRole) VALUES ('"+usLogin+"','"+ usPassword + "' ,'"  + usRole + "' )";
					int res = BD.executerUpdate(connexion, sql);
				break;
				case 2 :
					connected =0;
					//Affichage de connection
					Ecran.afficher("Saisir votre login : ");
					usLogin = Clavier.saisirString();
					Ecran.afficher("Saisir votre mot de passe : ");
					usPassword = Clavier.saisirString();

					//Verification correspondance entre login et mot de passe pour connection
					sql = "SELECT * FROM utilisateur WHERE usLogin = '"+usLogin+"' AND usPassword = '"+usPassword+"'";
					res = BD.executerSelect(connexion, sql);
					boolean login = BD.suivant(res);
					if (login) { 		//si l'identifiant et le mot de passe sont correct acces a l'interface utilisateur
						Ecran.afficherln("Vous etes maintenant connecte");
						connected = 1;
						disconnected = 2;
					}
					else { 		//mauvais identifiant et mot de passe retour au menu
						Ecran.afficherln("Erreur, identifiant ou mot de passe incorrect");
						connected = 0;
						disconnected = -2;
					}

					if(disconnected == 2){ //Si l'utilisateur est connecte
						while (connected != 0 ) { //Pour gere la fermeture de la fenetre interface
							sql = "SELECT * FROM utilisateur WHERE usRole = 'admin' AND usLogin ='"+usLogin+"'"; 
							res = BD.executerSelect(connexion, sql);
							boolean admin = BD.suivant(res);
							////////////////////// UTILISATEUR //////////////////////////////////
							Ecran.afficherln("Menu principale");    //Menu
							Ecran.afficherln(" 1 - Remplir son profil");      //Remplir le profil
							Ecran.afficherln(" 2 - Rechercher un g33k");   //Rechercher un g33k
							Ecran.afficherln(" 3 - Adopter un g33k");	   //Adopter un g33k
							Ecran.afficherln(" 4 - Liste des rendez-vous"); // Liste des rendez-vous
							////////////////// ADMINSTRATION////////////////////////////////////
							if (admin) {		//Si l'utilisateur est admin
								Ecran.afficherln(" 5 - Ajouter un utlisateur");   //Ajouter un utilisateur 			
								Ecran.afficherln(" 6 - Supprimer un utlisateur"); //Supprimer l'utilisateur
							}
							Ecran.afficherln(" 0 - Deconnexion");		   //Retour a Espace Membre
							
							connected = Clavier.saisirInt();
							if(admin != true){ //Restriction d'acces a la partie admin dans le menu principale
								if(connected < 0 || connected > 4){
									Ecran.afficherln("Choix incorrect: Resaisissez le numero !"); //Message d'erreur si choix incorrect
								}
							}
							switch (connected) {										//<- Switch du menu pour membre normal
								case 1 ://Remplir son profil
									//Supression des donnees du profil
									sql = "DELETE FROM profil WHERE prUtilisateur ='"+usLogin+"' ";
									res = BD.executerUpdate(connexion, sql);
									//Suppression des donnees de l'interet de l'utilisateur
									sql = "DELETE FROM interet_utilisateur WHERE iuUtilisateur ='"+usLogin+"' ";
									res = BD.executerUpdate(connexion, sql);
									//Login de la personne
									Ecran.afficherln("Votre login : ", usLogin);
									//Sexe de la personne
									Ecran.afficherln("Votre sexe  (1:homme , 2:femme) : ");
									int genre = Clavier.saisirInt();
									while(genre != 1 && genre != 2 ){
										Ecran.afficherln("Erreur: Saisie incorrect !");
										Ecran.afficher("Votre sexe  (1:homme , 2:femme) : ");
										genre = Clavier.saisirInt();
									}
										String prSexe;
									if (genre ==1){
										prSexe = "homme";
									}
									else{
										prSexe = "femme";
									}
									Ecran.afficher("Votre taille (en cm) : ");
									int prTaille = Clavier.saisirInt();
									//Couleur de cheveux
									Ecran.afficherln("Votre couleur de cheuveux");
									Ecran.afficherln("1 - Brun");
									Ecran.afficherln("2 - Blond");
									Ecran.afficherln("3 - Chatain");
									Ecran.afficherln("4 - Noir");
									Ecran.afficherln("5 - Roux");
									int cheveux = Clavier.saisirInt();
									while(cheveux<1 || cheveux > 5){
										Ecran.afficherln("Erreur: Saisie incorrect !");
										Ecran.afficherln("Votre couleur de cheuveux");
										Ecran.afficherln("1 - Brun");
										Ecran.afficherln("2 - Blond");
										Ecran.afficherln("3 - Chatain");
										Ecran.afficherln("4 - Noir");
										Ecran.afficherln("5 - Roux");
										cheveux = Clavier.saisirInt();
									}
									String prCheveux = "";
									switch(cheveux){
										case 1:
											prCheveux = "brun";
										break;
										case 2:
											prCheveux = "blond";
										break;
										case 3:
											prCheveux = "chatain";
										break;
										case 4:
											prCheveux = "noir";
										break;
										case 5:
											prCheveux = "roux";
										break;
									}
									//Poids de la persone
									Ecran.afficher("Votre poids (en kg) : ");
									int prPoids = Clavier.saisirInt();
									//Interet de la personne
									Ecran.afficherln("Votre interet : ");
									Ecran.afficherln("1 - Jeux videos PC");
									Ecran.afficherln("2 - Jeux videos Console");
									Ecran.afficherln("3 - Series");
									Ecran.afficherln("4 - Cinema");
									Ecran.afficherln("5 - RetroGaming");
									Ecran.afficherln("6 - Musique");
									Ecran.afficherln("7 - Anime / Manga");
									int interet = Clavier.saisirInt();
									while(interet<1 || interet>7){
										Ecran.afficherln("Erreur: Saisie incorrect !");
										Ecran.afficherln("Votre interet : ");
										Ecran.afficherln("1 - Jeux videos PC");
										Ecran.afficherln("2 - Jeux videos Console");
										Ecran.afficherln("3 - Series");
										Ecran.afficherln("4 - Cinema");
										Ecran.afficherln("5 - RetroGaming");
										Ecran.afficherln("6 - Musique");
										Ecran.afficherln("7 - Anime / Manga");
										interet = Clavier.saisirInt();
									}
									String iuInteret = "";
									switch(interet){
										case 1:
											iuInteret = "Jeux videos PC";
										break;
										case 2:
											iuInteret = "Jeux videos Console";
										break;
										case 3:
											iuInteret = "Series";
										break;
										case 4:
											iuInteret = "Cinema";
										break;
										case 5:
											iuInteret = "RetroGaming";
										break;
										case 6:
											iuInteret = "Musique";
										break;
										case 7:
											iuInteret = "Anime / Manga";
										break;
									}
									//Description de la personne
									Ecran.afficher("Description :");
									String prBio = Clavier.saisirString();
									
									sql = "INSERT INTO profil(prUtilisateur, prSexe, prTaille, prCheveux, prPoids, prBio) VALUES ('"+ usLogin +"' ,'"+prSexe+ "' ,'"  + prTaille + "' ,'" + prCheveux + "' ,'" + prPoids + "' ,'" + prBio + "')";
									res = BD.executerUpdate(connexion, sql);
									
									sql = "INSERT INTO interet_utilisateur(iuUtilisateur, iuInteret) VALUES ('" + usLogin + "' , '" + iuInteret + "')";
									res = BD.executerUpdate(connexion, sql);
									
								break;
								case 2 ://Rechercher un g33k
									Ecran.afficherln("Menu de recherche");//Menu
									Ecran.afficherln("1 - Selection par sexe");
									Ecran.afficherln("2 - Selection par couleur de cheveux");
									Ecran.afficherln("3 - Selection par interet");
									int search = Clavier.saisirInt();
									String recherche ="";
									while( search < 1 || search > 3){//Verification menu
										Ecran.afficherln("Erreur: Saisie incorrect");
										Ecran.afficherln("1 - Selection par sexe");
										Ecran.afficherln("2 - Selection par couleur de cheveux");
										Ecran.afficherln("3 - Selection par interet");
										search = Clavier.saisirInt();
									}
									switch(search){
										case 1://Rechercher par genre
										Ecran.afficherln("Selection par sexe");
											Ecran.afficherln("1 - Homme");
											Ecran.afficherln("2 - Femme");
											int sexe = Clavier.saisirInt();
											while( sexe < 1 || sexe > 2){//Verification menu selection
												Ecran.afficherln("Erreur: Saisie incorrect");
												Ecran.afficherln("Selection par sexe");
												Ecran.afficherln("1 - Homme");
												Ecran.afficherln("2 - Femme");
												sexe = Clavier.saisirInt();
											}
											switch(sexe){//Rechercher par rapport au choix
												case 1 :
													recherche = "homme";
												break;
												case 2 :
													recherche = "femme";
												break;
											}//Recherche des utilisateurs correspondant a la selection
											RechercherG33k(connexion, search, recherche);
											
										break;
										case 2://Selection par couleur de cheveux
											Ecran.afficherln("Selection par couleur de cheveux");
											Ecran.afficherln("1 - Brun");
											Ecran.afficherln("2 - Blond");
											Ecran.afficherln("3 - Chatain");
											Ecran.afficherln("4 - Noir");
											Ecran.afficherln("5 - Roux");
											int selCheveux = Clavier.saisirInt();
											while(selCheveux < 1 || selCheveux > 5){//Verification du choix
												Ecran.afficherln("Erreur: Saisie incorrect");
												Ecran.afficherln("Selection par couleur de cheveux");
												Ecran.afficherln("1 - Brun");
												Ecran.afficherln("2 - Blond");
												Ecran.afficherln("3 - Chatain");
												Ecran.afficherln("4 - Noir");
												Ecran.afficherln("5 - Roux");
												selCheveux = Clavier.saisirInt();
											}
											switch(selCheveux){//Recherche par rapport au choix
												case 1:
													recherche = "brun";
												break;
												case 2:
													recherche = "blond";
												break;
												case 3:
													recherche = "chatain";
												break;
												case 4:
													recherche = "noir";
												break;
												case 5:
													recherche = "roux";
												break;
											}
											RechercherG33k(connexion, search, recherche);
											
										break;
										case 3://Selection par interet
											Ecran.afficherln("Selection par interet");
											Ecran.afficherln("1 - Jeux videos PC");
											Ecran.afficherln("2 - Jeux videos Console");
											Ecran.afficherln("3 - Series");
											Ecran.afficherln("4 - Cinema");
											Ecran.afficherln("5 - RetroGaming");
											Ecran.afficherln("6 - Musique");
											Ecran.afficherln("7 - Anime / Manga");
											int selInteret = Clavier.saisirInt();
											while(selInteret < 1 || selInteret > 7){//Verification de la selection 
												Ecran.afficherln("Erreur: Saisie Incorrect");
												Ecran.afficherln("Selection par interet");
												Ecran.afficherln("1 - Jeux videos PC");
												Ecran.afficherln("2 - Jeux videos Console");
												Ecran.afficherln("3 - Series");
												Ecran.afficherln("4 - Cinema");
												Ecran.afficherln("5 - RetroGaming");
												Ecran.afficherln("6 - Musique");
												Ecran.afficherln("7 - Anime / Manga");
												selInteret = Clavier.saisirInt();
											}
											switch(selInteret){//Recherche des utilisateurs correspondants au critere
												case 1:
													recherche = "Jeux videos PC";
												break;
												case 2:
													recherche = "Jeux videos Console";
												break;
												case 3:
													recherche = "Series";
												break;
												case 4:
													recherche = "Cinema";
												break;
												case 5:
													recherche = "RetroGaming";
												break;
												case 6:
													recherche = "Musique";
												break;
												case 7:
													recherche = "Anime / Manga";
												break;
											}
											RechercherG33k(connexion, search, recherche);
											
										break;
									}
								break;
								case 3 ://Adopter un g33k puis retour au menu principale
									Ecran.afficherln("Saisir le login du g33k que vous souhaitez adopter !");
									String adopter;
									adopter = Clavier.saisirString();
								
									sql = "SELECT MAX(rdvID) FROM rendez_vous"; //recherche la valeur maximal de rdvID afin de determiner la prochaine valeur (+1)
									res = BD.executerSelect(connexion, sql);
									boolean bool = BD.suivant(res);
									int verif = BD.attributInt(res, "MAX(rdvID)") + 1;
										
									sql = "INSERT INTO rendez_vous(rdvID, rdvDemandeur, rdvCible, rdvReponse) VALUES ('"+verif+ "','"+usLogin+ "' ,'"  + adopter + "', NULL)";
									res = BD.executerUpdate(connexion, sql);
								
									
								break;
								case 4 :
									//Liste des rendez-vous puis retour au menu principale
									int rdv;
									Ecran.sautDeLigne();
									Ecran.afficher("Espace rendez-vous \n");
									Ecran.afficher("1 - Liste des rendez-vous \n");
									Ecran.afficher("2 - Gerer vos rendez-vous \n");
									rdv = Clavier.saisirInt();
									switch (rdv) {
										case 1 :
											sql = "SELECT * FROM rendez_vous WHERE rdvCible = '"+usLogin+"'";
											res = BD.executerSelect(connexion, sql);
											if (res >= 0){//Demandes recus
												Ecran.sautDeLigne();
												Ecran.afficher("Demandes recus : \n");
												while (BD.suivant(res)) {//Affichage des personnes qui ont envoye une demande de rendez-vous
													String rdvDemandeur = BD.attributString(res, "rdvDemandeur");
													Ecran.afficherln(rdvDemandeur, " veut un rendez-vous avec vous !");
													String rdvReponse = BD.attributString(res, "rdvReponse");
													if(rdvReponse == "accepte" || rdvReponse == "decline") { //HORS ENNONCE
														Ecran.afficherln("Votre reponse :", rdvReponse);
														Ecran.afficherln("-----------------------");
													}
													else {
														Ecran.afficherln("Vous n'avez pas encore repondu");
														Ecran.afficherln("-----------------------");
													}
												}
											}
											else {
												Ecran.afficher("Vous n'avez aucune demande de rendez-vous");
											}
											Ecran.sautDeLigne();
											sql = "SELECT * FROM rendez_vous WHERE rdvDemandeur = '"+usLogin+"'";
											res = BD.executerSelect(connexion, sql);
											if (res >= 0){//Demandes envoyees
												Ecran.afficher("Demandes envoyees : \n");
												while (BD.suivant(res)) {//Affichage des personnes qui ont ete invitees
													String rdvCible = BD.attributString(res, "rdvCible");
													Ecran.afficherln("Vous avez envoyer une invitation a ", rdvCible);
													String rdvReponse = BD.attributString(res, "rdvReponse");
													if(rdvReponse == "accepte" || rdvReponse == "decline") { //HORS ENNONCE
														Ecran.afficherln("Sa reponse : ", rdvReponse);
														Ecran.afficherln("-----------------------");
													}
													else {
														Ecran.afficherln("L'utilisateur ne vous a pas repondu");
														Ecran.afficherln("-----------------------");
													}
												}
											}
											else {
												Ecran.afficher("Vous n'avez envoyer aucune invitation ");
											}
										break;
										case 2 :
											Ecran.sautDeLigne();
											Ecran.afficherln("Saisir le nom de l'utilisateur pour repondre a son rendez-vous : ");
											String rdvDemandeur = Clavier.saisirString();
											sql = "SELECT * FROM rendez_vous WHERE rdvDemandeur = '"+rdvDemandeur+"' AND rdvCible = '"+usLogin+"'"; //Verification de l'existance de cette demande de rendez-vous
											res = BD.executerSelect(connexion, sql);
											boolean existRDV = BD.suivant(res);
											String rdvReponse = BD.attributString(res, "rdvReponse");
											if(existRDV) { //si la demande existe
												if(rdvReponse == "accepte" || rdvReponse == "decline") {
													Ecran.afficherln("Vous avez deja repondu a ce rendez vous");
												}
												else {
													Ecran.afficherln("Votre reponse : ");
													Ecran.afficherln("1 - Accepter l'invitation");
													Ecran.afficherln("2 - Decliner l'invitation");
													int repRdv = Clavier.saisirInt();
													if(repRdv == 1) {
														rdvReponse = "accepte";
													}
													else {
														rdvReponse = "decline";
													}
													
													sql = "UPDATE rendez_vous SET rdvReponse = '"+rdvReponse+"' WHERE rdvDemandeur = '"+rdvDemandeur+"' AND rdvCible = '"+usLogin+"' ";
													res = BD.executerUpdate(connexion, sql);
													Ecran.afficherln("Reponse prise en compte");
												}
											}
											else {
												Ecran.afficherln("Aucune demande recu pour cet utilisateur"); //Si la demande n'existe pas
											}
									}
								break;
								case 0 :
									//Retour a Espace Membre
									Ecran.afficher("Deconnexion en cours..\n");
									connected = 0;
									disconnected = -2;
								break;
							}
							if(admin){//<- Switch supplementaire pour administrateur pour acces au menu principale
								switch (connected) {
									case 5 :
										//Ajouter un membre puis retour au menu principale
									Ecran.afficher("Saisir le login : ");
									String usLoginAdmin = Clavier.saisirString();
									Ecran.afficher("Saisir le mot de passe : ");
									String usPasswordAdmin = Clavier.saisirString();
									Ecran.afficherln("Role de la personne (1 : admin 2 : aucun) : ");
									int usRoleChoix = Clavier.saisirInt();
									while(usRoleChoix != 1 && usRoleChoix != 2 ){//Verification du choix du role
										Ecran.afficherln("Erreur: Saisie incorrect !");
										Ecran.afficherln("Role de la personne (1 : admin 2 : aucun) : ");
										usRoleChoix = Clavier.saisirInt();
									}
									String usRoleAdmin;
									if (usRoleChoix ==1){
										usRoleAdmin = "admin";
									}
									else{
										usRoleAdmin = "aucun";
									}//Ajout dans la base de donnee
									
									sql = "INSERT INTO utilisateur(usLogin,usPassword,usRole,) VALUES ('"+usLoginAdmin+"','"+ usPasswordAdmin + "' ,'"  + usRoleAdmin + "' )";
									res = BD.executerUpdate(connexion, sql);
									Ecran.afficherln("Membre ajoute");
									Ecran.sautDeLigne();
									break;
									case 6 :
										// Supprimer un membre puis retour au menu principale
										Ecran.afficher("Login du compte a effacer : ");
										usLoginAdmin = Clavier.saisirString();
										sql = "DELETE FROM utilisateur WHERE usLogin ='"+usLoginAdmin+"' ";
										res = BD.executerUpdate(connexion, sql);
										sql = "DELETE FROM profil WHERE prUtilisateur ='"+usLoginAdmin+"' ";
										res = BD.executerUpdate(connexion, sql);
										sql = "DELETE FROM interet_utilisateur WHERE iuUtilisateur ='"+usLoginAdmin+"' ";
										res = BD.executerUpdate(connexion, sql);
										Ecran.afficherln("Membre supprime");
										Ecran.sautDeLigne();
										//ajouter la suppression au niveau des rendez vous une fois cette partie gere
									break;
								}
							}
						}
					}

				break;
				case 0 ://Quitter le programme
					disconnected = -1;
					Ecran.afficher("Quitter le programme...\n");
				break;
			}
		}
		
	}
}

