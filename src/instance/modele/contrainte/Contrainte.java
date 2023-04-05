package instance.modele.contrainte;

public abstract class Contrainte {

    protected boolean dure;
    public Contrainte(boolean dure) {
        this.dure=dure;
    }

    public TypeContrainte getTypeContrainte(){
        if(this instanceof ContrainteEquite)return TypeContrainte.EQUITE;
        if(this instanceof ContrainteHBClassement)return TypeContrainte.HBCLASSEMENT;
        if(this instanceof ContraintePauseEquipe)return TypeContrainte.PAUSEEQUIPE;
        if(this instanceof ContrainteEquite)return TypeContrainte.EQUITE;
        if(this instanceof ContrainteEquite)return TypeContrainte.EQUITE;
        if(this instanceof ContrainteEquite)return TypeContrainte.EQUITE;
        //todo
        return null;
    }
}
