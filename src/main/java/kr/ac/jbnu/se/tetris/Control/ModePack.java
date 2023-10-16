package kr.ac.jbnu.se.tetris.Control;

import kr.ac.jbnu.se.tetris.Boundary.TetrisCanvas;
import kr.ac.jbnu.se.tetris.Entity.Chromosome;
import kr.ac.jbnu.se.tetris.Entity.Entity;
import kr.ac.jbnu.se.tetris.Entity.Tetrominoes;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

import static kr.ac.jbnu.se.tetris.Boundary.TetrisCanvas.BoardHeight;
import static kr.ac.jbnu.se.tetris.Boundary.TetrisCanvas.BoardWidth;
import static kr.ac.jbnu.se.tetris.Entity.Chromosome.SIZE_GENE;

public class ModePack {

    Random random;
    Calculator calculator;
    TetrisCanvas canvas;
    public ModePack(TetrisCanvas canvas) {
        this.canvas = canvas;
        random = new Random();
        calculator = new Calculator(canvas,canvas.board);
    }
    private List<Chromosome> rouletteWheelSelection(List<Chromosome> generation, int num) {
        // 모든 유전자의 총 누적 적합도 계산
        int totalFitness = 0;
        for (Chromosome gene : generation) {
            //totalFitness += gene.fitness; origin code
            totalFitness += gene.getFitness();
        }

        List<Chromosome> ret = new ArrayList<>();

        for (int i = 0; i < num; i++) {
            // 0과 totalFitness 사이의 임의 값 선택
            double randomValue = Math.random() * totalFitness;

            // 룰렛 휠을 돌며 유전자 선택
            int fitnessSum = 0;
            for (Chromosome gene : generation) {
                //fitnessSum += gene.fitness; origin code
                fitnessSum += gene.getFitness();
                if (fitnessSum >= randomValue) {
                    ret.add(gene);
                }
            }
        }
        return ret;
    }
    private void sort(List<Chromosome> generation) {
        generation.sort(new Comparator<Chromosome>() {

            @Override
            public int compare(Chromosome arg0, Chromosome arg1) {
                // TODO Auto-generated method stub
                //return Integer.compare(arg1.fitness, arg0.fitness); origin code
                return Integer.compare(arg1.getFitness(), arg0.getFitness());
            }

        });
    }

    private void computeFitness(List<Chromosome> generation, int num_of_iter) {
        for (Chromosome chromosome : generation) {
//            CURCHROMOSOME = chromosome.number;
            for (int game = 0; game < num_of_iter; game++) {
//                ROUND = i + 1;
                int current_game_score = 0;
                outWhile:
                while (true) {
                    //Shape curPiece = new Shape(); origin code
                    Entity curPiece = new Entity(Tetrominoes.NoShape);
                    curPiece.setRandomShape();
                    //curPiece.setPosition(BoardWidth / 2 + 1, BoardHeight - 1 - curPiece.maxY());origin code
                    curPiece.setPosition(BoardWidth / 2 + 1, BoardHeight - 1 - curPiece.maxY());
                    //Shape goodPosition = new Shape(); origin code
                    Entity goodPosition = new Entity(Tetrominoes.NoShape);
                    double big_weight = Integer.MIN_VALUE;
//                    TIME =  2;

                    for (int j = 0; j < curPiece.getNumOfRotate(); j++) {
                        if (finish(curPiece))
                            break outWhile;

                        Object[] ret = move(curPiece, chromosome);

                        if (big_weight < (double) ret[0]) {
                            big_weight = (double) ret[0];

                            goodPosition = new Entity(((Entity) ret[1]).getShape());
                        }

                        // 블럭 회전
                        curPiece = curPiece.rotateRight();
//                        TIME = 0;
                    }

                    // 가장 최적의 위치에 블록을 둔다.
                    placeShape(goodPosition);

                    current_game_score += is_complete_line();
                }
                canvas.clearBoard();
                chromosome.updateFitness(chromosome.getFitness() + current_game_score);
            }
        }
    }

    // 지운 줄 수를 반환하는 메소드
    private int is_complete_line() {
        int numFullLines = 0;

        for (int i = BoardHeight - 1; i >= 0; --i) {
            int j;
            for (j = 0; j < BoardWidth; ++j) {
                if (canvas.shapeAt(j, i) == Tetrominoes.NoShape) {
                    break;
                }
            }

            if (j == BoardWidth) {
                ++numFullLines;
                for (int k = i; k < BoardHeight - 1; ++k) {
                    for (j = 0; j < BoardWidth; ++j)
                        canvas.board[(k * BoardWidth) + j] = canvas.shapeAt(j, k + 1);
                }
                for (j = 0; j < BoardWidth; ++j) {
                    canvas.board[((BoardHeight - 1) * BoardWidth) + j] = Tetrominoes.NoShape;
                }
            }
        }
        return numFullLines;
    }

    private Object[] move(Entity curPiece, Chromosome chromosome) {
        double big_weight = Integer.MIN_VALUE;
        //Shape big_weight_block = new Shape(); origin code
        Entity block, big_weight_block, temp_block;
        boolean end_down = false, end_right = false;

        block = new Entity(curPiece.getShape());
        move_left(block);
        big_weight_block = new Entity(Tetrominoes.NoShape); //ret[1] 구문 이니셜라이징이 필요하므로 넣어둠.
        temp_block = new Entity(block.getShape());
        canvas.setCurPiece(temp_block);
        while (true) {
            if (canvas.dropDown()) {
                // 블럭을 아래로 다 내렸을 경우, 현재 가중치 값 계산
                placeShape(temp_block);
                double fitness = calculator.blockFitness(chromosome.getWeight());
                if (big_weight < fitness) {
                    big_weight = fitness;
                    big_weight_block = new Entity(temp_block.getShape());
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

    private void deleteBlock(Entity shape) {
        int curX = shape.position[0];
        int curY = shape.position[1];
        //for(int i = 0; i < shape.coords.length; i++) { origin code
        for (int i = 0; i < shape.getShapeArr().length; i++) {
            int x = shape.getShapeArr()[i][0];
            int y = shape.getShapeArr()[i][1];
            canvas.board[(curY + y) * BoardWidth + (curX + x)] = Tetrominoes.NoShape;
        }
    }

    private void placeShape(Entity shape) {
        int curX = shape.position[0];
        int curY = shape.position[1];
        for (int i = 0; i < 4; i++) {
            int x = curX + shape.x(i);
            int y = curY - shape.y(i);
            int idx = y * BoardWidth + x;
            canvas.board[idx] = shape.getShape();
        }
    }

    private boolean move_right(Entity shape) {
        return !canvas.tryMove(shape, shape.position[0] + 1, shape.position[1]);
    }

    private void move_left(Entity shape) {
        int newX = shape.position[0];
        while (newX > 0) {
            if (!canvas.tryMove(shape, newX - 1, shape.position[1]))
                break;
            --newX;
        }
    }
    private boolean finish(Entity shape) {
        int x = shape.position[0];
        int y = shape.position[1];
        for (int i = 0; i < 4; i++) {
            if (canvas.shapeAt(x + shape.x(i), y + shape.y(i)) != Tetrominoes.NoShape)
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
    public Chromosome generateRandom(int number) {
        Random random = new Random();
        double[] weight = new double[SIZE_GENE];
        for (int i = 0; i < SIZE_GENE; i++) {
            weight[i] = Math.random() - 0.5;
        }

        Chromosome chromosome = new Chromosome(number, weight);
        chromosome.normalize();
        return chromosome;
    }

    public List<Chromosome> initWeight(int size) {
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
                //final_candidate.number = i + 1; origin code
                final_candidate.updateNumber(i + 1);//update메서드로 값 바꿈. 오류시 오리진 코드로 수정요망
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
                total_fitness += generation.get(i).getFitness();//fitness를 가져오기 위해 public getter 메소드 추가함. fitness -> private로 변경
            }

            System.out.println("Avg fitness : " + total_fitness / generation.size());
            System.out.println("Best fitness : " + generation.get(0).getFitness());
            System.out.println("Best Gene: #" + generation.get(0).getNumber());

            resetNumber(generation);
//            GENERATION++;
        }
        System.out.println("Best fitness : " + generation.get(0).getFitness());
        for (int i = 0; i < SIZE_GENE; i++) {
            System.out.println("weight[" + i + "] = " + generation.get(0).getWeight()[i]);//여기도 getter 메소드. weight -> private로 변경
        }
        return generation;
    }
    private void mutation(Chromosome candidate) {
        double mutation = Math.random() * 0.5 * 2 - 0.5;
        int idx = random.nextInt(4);
        //candidate.weight[idx] += mutation; origin code
        candidate.getWeight()[idx] += mutation;
    }
    private Chromosome cross_over(Chromosome gene1, Chromosome gene2) {
        double[] weight = new double[SIZE_GENE];

        for(int i = 0; i < SIZE_GENE; i++) {
            //weight[i] = gene1.weight[i] * gene1.fitness + gene2.weight[i] * gene2.fitness; origin code
            weight[i] = gene1.getWeight()[i] * gene1.getFitness() + gene2.getWeight()[i] * gene2.getFitness();

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
    private void deleteLowFitness(List<Chromosome> candidate, List<Chromosome> parent) {
        parent= parent.subList(0, parent.size()-candidate.size());
        parent.addAll(candidate);

        sort(parent);
    }
    private void resetNumber(List<Chromosome> generation) {
        for (int i = 0; i < generation.size(); i++) {
            //generation.get(i).number = i + 1; origin code
            generation.get(i).updateNumber(i+1);
        }
    }
}