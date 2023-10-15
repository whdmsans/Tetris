package kr.ac.jbnu.se.tetris.TetrisBot;

import kr.ac.jbnu.se.tetris.Shape;
import kr.ac.jbnu.se.tetris.Tetris;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

import javax.swing.*;

import static kr.ac.jbnu.se.tetris.TetrisBot.Chromosome.SIZE_GENE;

public class TetrisBotMode extends JPanel {

    int[] board;

    final int BoardWidth = 10;
    final int BoardHeight = 22;


    boolean isFallingFinished = false;
    int numLinesRemoved = 0;
    int curX = 0;
    int curY = 0;
    JLabel statusbar;

    Calculator calculator;

    Random random;

    public TetrisBotMode(Tetris parent) {
        statusbar = parent.getStatusBar();
        board = new int[BoardWidth * BoardHeight];
        clearBoard();

        random = new Random();
        calculator = new Calculator(board, BoardWidth, BoardHeight);
    }
    int shapeAt(int x, int y) {
        return board[(y * BoardWidth) + x];
    }

    public void start(int size) {
        clearBoard();
        numLinesRemoved = 0;

        List<Chromosome> generation = new ArrayList<>();
        for (int i = 0; i < size - 1; i++) {
            generation.add(generateRandom(i + 1));
        }


        double[] weight = new double[SIZE_GENE];
        //계산된 가중치값
        weight[0] = -0.8882324104022858;
        weight[1] = 0.3221180915759138;
        weight[2] = -0.2322970072064213;
        weight[3] = 0.2309138814220062;

        generation.add(generateChromosome(size, weight));

        computeFitness(generation, 10);
        sort(generation);

        //while (true) {
        for (int generationChange = 0; generationChange < 10; generationChange++) {
            List<Chromosome> candidate = new ArrayList<>();

            // 30% 유전자 선택
            for (int i = 0; i < (int) (size * 0.3); i++) {
//                Chromosome[] temp = tournamentSelection(generation, (int) (size * 0.1));
                List<Chromosome> temp = rouletteWheelSelection(generation, 2);
                Chromosome final_candidate = cross_over(temp.get(0), temp.get(1));
                final_candidate.number = i + 1;
                // 5% 변이
                if (Math.random() < 0.05) {
                    mutation(final_candidate);
                }
                final_candidate.normalize();
                // 후보 유전자 생성
                candidate.add(final_candidate);
            }

            System.out.println("Generate candidate gene : #" + candidate.size());
            computeFitness(candidate, 100);
            deleteLowFitness(candidate, generation);

            int total_fitness = 0;

            for (int i = 0; i < generation.size(); i++) {
                total_fitness += generation.get(i).fitness;
            }

            System.out.println("Avg fitness : " + total_fitness / generation.size());
            System.out.println("Best fitness : " + generation.get(0).fitness);
            System.out.println("Best Gene: #" + generation.get(0).number);

            resetNumber(generation);
//            GENERATION++;
        }

        System.out.println("Best fitness : " + generation.get(0).fitness);
        for (int i = 0; i < SIZE_GENE; i++) {
            System.out.println("weight[" + i + "] = " + generation.get(0).weight[i]);
        }
    }

    private void resetNumber(List<Chromosome> generation) {
        for (int i = 0; i < generation.size(); i++) {
            generation.get(i).number = i + 1;

        }
    }

    private void deleteLowFitness(List<Chromosome> candidate, List<Chromosome> parent) {
        parent= parent.subList(0, parent.size()-candidate.size());
        parent.addAll(candidate);

        sort(parent);
    }

    private void mutation(Chromosome candidate) {
        double mutation = Math.random() * 0.5 * 2 - 0.5;
        int idx = random.nextInt(4);
        candidate.weight[idx] += mutation;
    }

    private Chromosome cross_over(Chromosome gene1, Chromosome gene2) {
        double[] weight = new double[SIZE_GENE];

        for(int i = 0; i < SIZE_GENE; i++) {
            weight[i] = gene1.weight[i] * gene1.fitness + gene2.weight[i] * gene2.fitness;
        }

//        int select = random.nextInt(SIZE_GENE);
//        for (int i = 0; i < select; i++) {
//            weight[i] = gene1.weight[i];
//        }
//        for (int i = select; i < SIZE_GENE; i++) {
//            weight[i] = gene2.weight[i];
//        }

        return new Chromosome(0, weight);
    }

    private List<Chromosome> rouletteWheelSelection(List<Chromosome> generation, int num) {
        // 모든 유전자의 총 누적 적합도 계산
        int totalFitness = 0;
        for (Chromosome gene : generation) {
            totalFitness += gene.fitness;
        }

        List<Chromosome> ret = new ArrayList<>();

        for (int i = 0; i < num; i++) {
            // 0과 totalFitness 사이의 임의 값 선택
            double randomValue = Math.random() * totalFitness;

            // 룰렛 휠을 돌며 유전자 선택
            int fitnessSum = 0;
            for (Chromosome gene : generation) {
                fitnessSum += gene.fitness;
                if (fitnessSum >= randomValue) {
                    ret.add(gene);
                }
            }
        }

        return ret;
    }

//    private Chromosome[] tournamentSelection(List<Chromosome> generation, int num_of_tournament) {
//        Chromosome gene1 = null, gene2 = null;
//        int select_gene1 = generation.size(), select_gene2 = generation.size();
//
//        while (true) {
//            for (int i = 0; i < num_of_tournament; i++) {
//                int select = random.nextInt(generation.size());
//
//                if (gene1 == null || select_gene1 > select) {
//                    select_gene2 = select_gene1;
//                    gene2 = gene1;
//
//                    gene1 = generation.get(select);
//                    select_gene1 = select;
//                } else if (gene2 == null || select_gene2 > select) {
//                    gene2 = generation.get(select);
//                    select_gene2 = select;
//                }
//            }
//
//            if (gene1.fitness != 0 || gene2.fitness != 0)
//                break;
//        }
//
//        return new Chromosome[] { gene1, gene2 };
//    }

    private void sort(List<Chromosome> generation) {
        generation.sort(new Comparator<Chromosome>() {

            @Override
            public int compare(Chromosome arg0, Chromosome arg1) {
                // TODO Auto-generated method stub
                return Integer.compare(arg1.fitness, arg0.fitness);
            }

        });
    }

    private void computeFitness(List<Chromosome> generation, int num_of_iter) {
        for(Chromosome chromosome : generation) {
//            CURCHROMOSOME = chromosome.number;
            for(int game = 0; game < num_of_iter; game++) {
//                ROUND = i + 1;
                int current_game_score = 0;
                outWhile: while (true) {
                    Shape curPiece = new Shape();
                    curPiece.setRandomShape();
                    curPiece.setPosition(BoardWidth / 2 + 1, BoardHeight - 1 - curPiece.maxY());
                    Shape goodPosition = new Shape();
                    double big_weight = Integer.MIN_VALUE;
//                    TIME =  2;

                    for (int j = 0; j < curPiece.getNumOfRotate(); j++) {
                        if (finish(curPiece))
                            break outWhile;

                        Object[] ret = move(curPiece, chromosome);

                        if (big_weight < (double) ret[0]) {
                            big_weight = (double) ret[0];
                            goodPosition.copyShape((Shape) ret[1]);;
                        }

                        // 블럭 회전
                        curPiece = curPiece.rotateRight();
//                        TIME = 0;
                    }

                    // 가장 최적의 위치에 블록을 둔다.
                    placeShape(goodPosition);

                    current_game_score += is_complete_line();
//                    current_game_score -= avg_hight();
//                    BEST_LINE = Math.max(BEST_LINE, current_game_score);
//                    CURRENT_LINE = current_game_score;
//                    t_board.repaint();
                }
                clearBoard();
                chromosome.fitness += current_game_score;
            }
        }
    }
//
//    private int avg_hight() {
//        int[] height = new int[BoardWidth];
//        for (int i = 0; i < BoardWidth; i++) {
//            int high = BoardHeight - 1;
//            while (high >= 0) {
//                if (board[high * BoardWidth + i] != 0) {
//                    break;
//                }
//                high--;
//            }
//            height[i] = high + 1;
//        }
//
//        int ret = 0;
//        for (int i = 0; i < BoardWidth; i++) {
//            ret += height[i];
//        }
//        return ret / BoardWidth;
//    }

    // 지운 줄 수를 반환하는 메소드
    private int is_complete_line() {
        int numFullLines = 0;

        for (int i = BoardHeight - 1; i >= 0; --i) {
            int j;
            for (j = 0; j < BoardWidth; ++j) {
                if (shapeAt(j, i) == 0) {
                    break;
                }
            }

            if (j == BoardWidth) {
                ++numFullLines;
                for (int k = i; k < BoardHeight - 1; ++k) {
                    for (j = 0; j < BoardWidth; ++j)
                        board[(k * BoardWidth) + j] = shapeAt(j, k + 1);
                }
                for (j = 0; j < BoardWidth; ++j) {
                    board[((BoardHeight - 1) * BoardWidth) + j] = 0;
                }
            }
        }
        return numFullLines;
    }

    private Object[] move(Shape curPiece, Chromosome chromosome) {
        double big_weight = Integer.MIN_VALUE;
        Shape big_weight_block = new Shape();
        boolean end_down = false, end_right = false;

        Shape block = new Shape();
        block.copyShape(curPiece);
        move_left(block);
        Shape temp_block = new Shape();

        while (true) {
            temp_block.copyShape(block);
            if (end_down = dropDown(temp_block)) {
                // 블럭을 아래로 다 내렸을 경우, 현재 가중치 값 계산
                placeShape(temp_block);
                double fitness = calculator.blockFitness(chromosome.weight);
                if (big_weight < fitness) {
                    big_weight = fitness;
                    big_weight_block.copyShape(temp_block);
                }
                deleteBlock(temp_block);
            }
            // 그 후, 우측으로 이동
            end_right = move_right(block);
            if (end_down && end_right) {
                break;
            }
        }

        Object[] ret = new Object[2];
        ret[0] = big_weight;
        ret[1] = big_weight_block;
        return ret;
    }

    private void deleteBlock(Shape shape) {
        int curX = shape.position[0];
        int curY = shape.position[1];
        for(int i = 0; i < shape.coords.length; i++) {
            int x = shape.coords[i][0];
            int y = shape.coords[i][1];
            board[(curY + y) * BoardWidth + (curX + x)] = 0;
        }
    }

    private void placeShape(Shape shape) {
        int curX = shape.position[0];
        int curY = shape.position[1];
        for(int i = 0; i < 4; i++) {
            int x = curX + shape.x(i);
            int y = curY - shape.y(i);
            int idx = y * BoardWidth + x;
            board[idx] = shape.getBlockNum();
        }
    }

    private boolean move_right(Shape shape) {
        return !tryMove(shape, shape.position[0] + 1 , shape.position[1]);
    }

    private void move_left(Shape shape) {
        int newX = shape.position[0];
        while (newX > 0) {
            if (!tryMove(shape, newX - 1 , shape.position[1]))
                break;
            --newX;
        }
    }

    private boolean finish(Shape shape) {
//        if (!tryMove(shape, shape.position[0], shape.position[1])) {
//            return true;
//        }
        int x = shape.position[0];
        int y = shape.position[1];
        for (int i = 0; i < 4; i++) {
            if (shapeAt(x + shape.x(i), y + shape.y(i)) != 0)
                return true;
        }
        return false;
    }

    private Chromosome generateChromosome(int number, double[] weight) {

        for (int i = 0; i < SIZE_GENE; i++) {
            weight[i] = random.nextInt(20) - 10;
        }

        Chromosome chromosome = new Chromosome(number, weight);
        chromosome.normalize();
        return chromosome;
    }
    private Chromosome generateRandom(int number) {
        Random random = new Random();
        double[] weight = new double[SIZE_GENE];
        for (int i = 0; i < SIZE_GENE; i++) {
            weight[i] = Math.random() - 0.5;
        }

        Chromosome chromosome = new Chromosome(number, weight);
        chromosome.normalize();
        return chromosome;
    }

    private Boolean dropDown(Shape curPiece) {
        int newY = curPiece.position[1];
        while (newY > 0) {
            if (!tryMove(curPiece, curPiece.position[0], --newY))
                break;
        }
        return true;
    }

    private void clearBoard() {
        for (int i = 0; i < BoardHeight * BoardWidth; ++i)
            board[i] = 0;
    }

    private boolean tryMove(Shape newPiece, int newX, int newY) {
        for (int i = 0; i < 4; ++i) {
            int x = newX + newPiece.x(i);
            int y = newY - newPiece.y(i);
            if (x < 0 || x >= BoardWidth || y < 0 || y >= BoardHeight)
                return false;
            if (shapeAt(x, y) != 0)
                return false;
        }
//        curPiece = newPiece;
        newPiece.setPosition(newX, newY);
        return true;
    }
}