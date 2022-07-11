package Player;

public class Player {

        private int posx;
        private int posy;
        private int nrTreasure;
        private int nrMoves;
        public final String playerIcon = "P";

        public Player(int posx, int posy){
                nrTreasure = 0;
                nrMoves = 0;
                this.posx = posx;
                this.posy = posy;
        }

        public int getPosx() {
                return posx;
        }

        public int getPosy() {
                return posy;
        }

        public void setPosx(int posx) {
                this.posx = posx;
        }

        public void setPosy(int posy) {
                this.posy = posy;
        }

        public void moveLeft(int left){
                setPosy(posy - left);
        }

        public void moveRight(int right){
                setPosy(posy + right);
        }

        public void moveUp(int up){
                setPosx(posx - up);
        }

        public void moveDown(int down){
                setPosx(posx + down);
        }

        public void foundTreasure(){
                nrTreasure++;
        }
        public void updateMoves(){
                nrMoves++;
        }

        public int getNrTreasure() {
                return nrTreasure;
        }

        public int getNrMoves() {
                return nrMoves;
        }

}
