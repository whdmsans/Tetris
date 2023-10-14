package kr.ac.jbnu.se.tetris;

public class Account {
    private String id, pw;
    private int score;
    public Account(String id, String pw){
        setUserId(id);
        updateUserPw(pw);
    }
    public int getUserScore(){ return score; }
    protected void updateUserScore(int score){ this.score = score; }
    protected void updateUserPw(String password){
        String pattern = "^[0-9|a-z|A-Z|ㄱ-ㅎ|ㅏ-ㅣ|가-힣]*$";
        //if(Pattern.matches())
        //this.password = password;
        this.pw = password;
    }
    protected void setUserId(String id){this.id = id;}
}
