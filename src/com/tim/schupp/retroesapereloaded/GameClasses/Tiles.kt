package com.tim.schupp.retroesapereloaded.GameClasses

import com.tim.schupp.retroesapereloaded.GameClasses.AllTiles.*
import com.tim.schupp.retroesapereloaded.MainGame

class Tiles {

    val tileAmount = 16
    lateinit var map: Map
    lateinit var game: MainGame
    /**
     * See AllTiles Package
     *ID:
     * 0: Empty Tile
     *
     * 1: START
     * 2: END
     * 3: Ground Tile
     * 4: Wall Tile
     * 5: Shield Tile
     * 6: Portal Tile
     * 7: Delete Tile
     * 8: Checkpoint Tile
     *
     * 9: Bomb Item
     *10: Bomb Wall Tile
     *11: Red Key Item
     *12: Red Key Door
     *13: Green Key Item
     *14: Green Key Door
     *15: Blue Key Item
     *16: Blue Key Door
     *
     */
    fun changeMap(newMap: Map){
        map = newMap
    }

    fun updateGame(newGame: MainGame){
        game = newGame
    }

    //TODO: DO full Tile Configuration and Make missing Textures
    fun newTileById(x: Int, y: Int, ID: Int): Tile{
        return when(ID){
            -1->SelectTile(x,y,map,game)//DONE
            0->EmptyTile(x,y,map,game)//DONE

            1->START(x,y,map,game)//DONE
            2->END(x,y,map,game)//DONE
            3->Ground(x,y,map,game)//DONE
            4->Wall(x,y,map,game)//DONE
            5->Shield(x,y,map,game)//DONE
            6->Portal(x,y,map,game)//DONE
            7->Delete(x,y,map,game)//DONE //NO TEXTURE
            8->Checkpoint(x,y,map,game)

            9->BombItem(x,y,map,game)//DONE
            10->BombWall(x,y,map,game)//DONE //NO TEXTURE
            11->RedKeyItem(x,y,map,game)//DONE
            12->RedDoor(x,y,map,game)//DONE
            13->GreenKeyItem(x,y,map,game)//DONE
            14->GreenDoor(x,y,map,game)//DONE
            15->BlueKeyItem(x,y,map,game)//DONE
            16->BlueDoor(x,y,map,game)//DONE
            else -> EmptyTile(x, y, map,game)
        }
    }
}