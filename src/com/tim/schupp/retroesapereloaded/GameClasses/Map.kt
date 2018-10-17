package com.tim.schupp.retroesapereloaded.GameClasses

import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.utils.Json
import com.tim.schupp.retroesapereloaded.MainGame

class Map(var game: MainGame,var width: Int, var height: Int) {

    var layers: Array<LevelLayer> //There will be a ground layer and an upper layer at least

    init {
        layers = arrayOf(LevelLayer(width,height,game),LevelLayer(width,height,game))
        game.tiles.updateGame(game)
        game.tiles.changeMap(this)
    }

    fun get(x:Int, y:Int): Array<Tile>{ //Returns the two tiles on that Position
        return arrayOf(layers[0].getTile(x,y)!!,layers[1].getTile(x,y)!!)
    }

    /** A game level layer **/
    class LevelLayer(var width: Int, var height: Int, var game: MainGame){

        var tiles: Array<Array<Tile?>> = Array(width) { Array<Tile?>(height) {null} }
        var tileIDS: Array<Array<Int>> = Array(width) { Array<Int>(height) {0}}
        var tileData: Array<Array<String>> = Array(width){ Array(height){""} }
        var justDrawVisibleTiles = true

        fun setTile(tile: Tile){
            tiles[tile.x][tile.y] = tile
            tileIDS[tile.x][tile.y] = tile.tileID
            tileData[tile.x][tile.y] = tile.tileInfo //Giving the Tile Meta Data
        }

        fun getTilesById(id: Int): Array<Tile>{
            var tileList = ArrayList<Tile>()
            for (tileRow in tiles){
                for(tile in tileRow) {
                    if(tile!= null&&tile.tileID==id){
                        tileList.add(tile)
                    }
                }
            }
            return tileList.toTypedArray()
        }

        fun getTile(x:Int,y:Int): Tile{
            return tiles[x][y]!!
        }

        fun isFieledEmpy(x: Int, y: Int): Boolean{
            return (tiles[x][y]==null)||(tiles[x][y]!!.tileID==0)
        }

        fun draw(batch: SpriteBatch){
            for (tileRow in tiles){
                for(tile in tileRow) {
                    if(tile!= null){
                        if(justDrawVisibleTiles){
                            if(tile.visible) tile.draw(batch)
                        }else{
                            tile.draw(batch)
                        }
                    }
                }
            }
        }

        //Loads the Game Tiles when a new Map is given
        fun loadTilesFromIds(newIDS: Array<Array<Int>>){
            tileIDS = newIDS
            for((y, line) in tileIDS.iterator().withIndex()){
                for((x, value) in line.iterator().withIndex()){
                    setTile(game.tiles.newTileById(x,y,
                            newIDS[x][y]))
                }
            }
        }

        fun containsTileWithId(id: Int): Boolean{
            for (tileRow in tiles){
                for(tile in tileRow) {
                    if(tile!= null&&tile.tileID == id) {
                        return true
                    }
                }
            }
            return false
        }
    }

    fun containsTileWithID(id: Int): Boolean{
        return layers[0].containsTileWithId(id)||layers[1].containsTileWithId(id)
    }

    fun findTilesWithId(id: Int): Array<Tile>{// tile[x][y][z]
        var listTilesLayer0 = layers[0].getTilesById(id)
        var listTilesLayer1 = layers[1].getTilesById(id)
        return Array<Tile>(listTilesLayer0.size+listTilesLayer1.size) {num ->
            if(num<listTilesLayer0.size){
                listTilesLayer0[num]
            }else{
                listTilesLayer1[num-listTilesLayer0.size]
            }
        }
    }

    fun putTile(tile: Tile){
        if(tile.layer == 0){
            if(layers[1].isFieledEmpy(tile.x,tile.y)||layers[1].getTile(tile.x,tile.y).allowTileOnOtherLayer)
                if(tile.canBeDropped(this)) {
                    layers[tile.layer].setTile(tile)
                }
        }else if(tile.layer == 1){
            if(layers[0].isFieledEmpy(tile.x,tile.y)||layers[0].getTile(tile.x,tile.y).allowTileOnOtherLayer)
                if(tile.canBeDropped(this)) {
                    layers[tile.layer].setTile(tile)
                }
        }

        game.tiles.changeMap(this)
    }

    var jsonParser = Json()
    fun convertToText(player: Player?): String{
        //TODO Add 6 for Player Items
        return jsonParser.toJson(Array(7) {num ->
            when(num){
                0->width.toString()
                1->height.toString()
                2->if(player!=null)jsonParser.toJson(player!!.playerItems) else "[0,0,0,0]"
                3->jsonParser.toJson(layers[0].tileIDS).toString()
                4->jsonParser.toJson(layers[1].tileIDS).toString()
                5->jsonParser.toJson(layers[0].tileData).toString()
                6-> jsonParser.toJson(layers[1].tileData).toString()
                else ->""   //All Part should always exist
            }
        })
    }

    var playerItems = IntArray(4)
    fun loadMapFromString(mapString: String){
        var allParts = jsonParser.fromJson((Array(7){""}).javaClass,mapString) //Width, Height,Items, Layer1, Layer2, InfoLayer1, InfoLayer2
        width = allParts[0].toInt()
        height = allParts[1].toInt()
        playerItems = jsonParser.fromJson(IntArray(4).javaClass,allParts[2])
        layers = arrayOf(LevelLayer(width,height,game),LevelLayer(width,height,game)) //Reloads the Layers in case the game dimensions changed
        var tileIDS1: Array<Array<Int>> = jsonParser.fromJson((Array(width) { Array(height) {0} }).javaClass,allParts[3])
        var tileIDS2: Array<Array<Int>> = jsonParser.fromJson((Array(width) { Array(height) {0} }).javaClass,allParts[4])
        layers[0].loadTilesFromIds(tileIDS1)
        layers[1].loadTilesFromIds(tileIDS2)
        //Putting the Tile Information:
        layers[0].tileData = jsonParser.fromJson((Array(width) { Array(height) {""} }).javaClass,allParts[5])
        layers[1].tileData = jsonParser.fromJson((Array(width) { Array(height) {""} }).javaClass,allParts[6])
    }

    fun saveMapState(player: Player): String{
        //In game save of the game map for Checkpoints and reloading
        //Delete old Start Tile:
        var foundTiles = findTilesWithId(6)     //Getting the Start Tile
        //Deleting it:
        layers[foundTiles[0].layer].setTile(game.tiles.newTileById(player.x,player.y,0))
        putTile(game.tiles.newTileById(player.x,player.y,6))//Set the new Start Position

        //After this modifications the Map can be converted Back To Text
        return convertToText(player)
    }

    fun deleteTile(x: Int, y: Int, layer1: Boolean, layer2: Boolean){
        layers[0].setTile(game.tiles.newTileById(x,y,0))
        layers[1].setTile(game.tiles.newTileById(x,y,0))
        game.tiles.changeMap(this)
    }
}