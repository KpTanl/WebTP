package utcapitole.miage.tp2.model;

public class Reservation {
    private int nbPersonnes;
    private String dateReservation;
    private String destination;
    private String nom;
    private String email;
    private String paiement;
    private String iban;
    private boolean offres;
    private boolean conditions;

    public Reservation() {
    }

    public Reservation(int nbPersonnes, String dateReservation, String destination, String nom, String email,
            String paiement, String iban, boolean offres, boolean conditions) {
        this.nbPersonnes = nbPersonnes;
        this.dateReservation = dateReservation;
        this.destination = destination;
        this.nom = nom;
        this.email = email;
        this.paiement = paiement;
        this.iban = iban;
        this.offres = offres;
        this.conditions = conditions;
    }

    public int getNbPersonnes() {
        return nbPersonnes;
    }

    public void setNbPersonnes(int nbPersonnes) {
        this.nbPersonnes = nbPersonnes;
    }

    public String getDateReservation() {
        return dateReservation;
    }

    public void setDateReservation(String dateReservation) {
        this.dateReservation = dateReservation;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPaiement() {
        return paiement;
    }

    public void setPaiement(String paiement) {
        this.paiement = paiement;
    }

    public String getIban() {
        return iban;
    }

    public void setIban(String iban) {
        this.iban = iban;
    }

    public boolean isOffres() {
        return offres;
    }

    public void setOffres(boolean offres) {
        this.offres = offres;
    }

    public boolean isConditions() {
        return conditions;
    }

    public void setConditions(boolean conditions) {
        this.conditions = conditions;
    }

    @Override
    public String toString() {
        return "Reservation [nbPersonnes=" + nbPersonnes + ", dateReservation=" + dateReservation
                + ", destination=" + destination + ", nom=" + nom + ", email=" + email + ", paiement=" + paiement
                + ", iban=" + iban + ", offres=" + offres + ", conditions=" + conditions + "]";
    }
}