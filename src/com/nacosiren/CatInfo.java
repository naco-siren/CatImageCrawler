package com.nacosiren;

/**
 * Created by naco_siren on 4/21/17.
 */
public class CatInfo {
    // Essential
    int _index;
    String _name;
    CatGender _catGender;
    String _imageHref;

    // Useful
    String _breed;
    String _color;
    String _age;

    // Others
    String _specHref;
    String _description;


    public CatInfo(int index, String name, CatGender catGender, String imageHref){
        this._index = index;
        this._name = name;
        this._catGender = catGender;
        this._imageHref = imageHref;
    }

    @Override
    public String toString() {
        return "#" + _index + " [" + _name + "] [" + _description + "]";
    }
}