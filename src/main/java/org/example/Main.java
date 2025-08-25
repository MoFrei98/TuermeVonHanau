package org.example;

import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    private static ArrayList<Ring> turm = null;
    private static ArrayList<Ring> pfahl1;
    private static ArrayList<Ring> pfahl2;
    private static ArrayList<Ring> pfahl3;

    private static String rep(String s, int count) {
        return count > 0 ? s.repeat(count) : "";
    }

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        System.out.print("Anzahl Ringe: ");
        int anzahlRinge = sc.nextInt();
        int minimalZuege = (1 << anzahlRinge) - 1;

        // prepare initial tower
        turm = new ArrayList<>(anzahlRinge);
        for (int i = anzahlRinge - 1; i >= 0; i--) {
            Ring ring = new Ring(i + 1);
            ring.setPosition(i);
            turm.add(ring);
        }

        // initialize pegs
        pfahl1 = new ArrayList<>(turm);
        pfahl2 = new ArrayList<>();
        pfahl3 = new ArrayList<>();

        boolean won = false;
        int anzahlZuege = 0;
        // game loop
        while (!won) {
            anzahlZuege++;
            printTurm();
            System.out.print("\n" + "\n" + "Zug eingeben (RingLabel ZielPfahl), z.B. A 3: ");
            String label = sc.next();
            int ziel = sc.nextInt();
            ArrayList<Ring> src = null;
            // determine source peg by top ring label
            if (!pfahl1.isEmpty() && pfahl1.get(pfahl1.size() - 1).getLabel().equalsIgnoreCase(label)) src = pfahl1;
            else if (!pfahl2.isEmpty() && pfahl2.get(pfahl2.size() - 1).getLabel().equalsIgnoreCase(label)) src = pfahl2;
            else if (!pfahl3.isEmpty() && pfahl3.get(pfahl3.size() - 1).getLabel().equalsIgnoreCase(label)) src = pfahl3;
            else {
                System.out.println("\n" + "\n" + "Kein oberer Ring mit Label " + label);
                continue;
            }
            // perform move
            Ring moving = src.remove(src.size() - 1);
            ArrayList<Ring> dest;
            switch (ziel) {
                case 1: // Ungueltiger Pfahl replace lower below
                    dest = pfahl1;
                    break;
                case 2:
                    dest = pfahl2;
                    break;
                case 3:
                    dest = pfahl3;
                    break;
                default:
                    System.out.println("\n" + "\n" + "Ungueltiger Pfahl: " + ziel);
                    src.add(moving);
                    continue;
            }
            // validate move
            if (dest.isEmpty() || dest.get(dest.size() - 1).getGroesse() > moving.getGroesse()) {
                dest.add(moving);
            } else {
                System.out.println("\n" + "\n" + "Ungueltiger Zug: groesserer Ring auf kleinerem");
                src.add(moving);
            }
            // check win
            if (pfahl3.size() == turm.size()) {
                printTurm();
                System.out.println("\n\nGEWONNEN!");
                System.out.println("Anzahl Züge: " + anzahlZuege);
                System.out.println("Minimale Anzahl Zuege: " + minimalZuege);
                // Simulation prompt
                System.out.print("Simulation der optimalen Loesung anzeigen? (J/N): ");
                String sim = sc.next();
                if (sim.equalsIgnoreCase("J")) {
                    // reset pegs
                    pfahl1.clear(); pfahl2.clear(); pfahl3.clear();
                    pfahl1.addAll(turm);
                    printTurm();
                    try {
                        simulateOptimal(anzahlRinge, pfahl1, pfahl2, pfahl3, 1, 2, 3);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
                won = true;
                break;
            }
        }
    }

    private static void printTurm() {
        int maxHeight = turm.size();
        int maxRingWidth = 2 * maxHeight + 1;


        for (int level = maxHeight - 1; level >= 0; level--) {
            // peg 1
            if (pfahl1.size() > level) {
                Ring ring = pfahl1.get(level);
                int size = ring.getGroesse();
                String stars = rep("*", size) + ring.getLabel() + rep("*", size);
                int printWidth = size * 2 + 1;
                int padLeft = Math.max(0, (maxRingWidth - printWidth) / 2);
                int padRight = Math.max(0, maxRingWidth - printWidth - padLeft);
                System.out.print(rep(" ", padLeft) + stars + rep(" ", padRight) + "\t\t");
            } else {
                int pad = Math.max(0, (maxRingWidth - 1) / 2);
                System.out.print(rep(" ", pad) + "|" + rep(" ", pad) + "\t\t");
            }
            // peg 2
            if (pfahl2.size() > level) {
                Ring ring2 = pfahl2.get(level);
                int size2 = ring2.getGroesse();
                String stars2 = rep("*", size2) + ring2.getLabel() + rep("*", size2);
                int printWidth2 = size2 * 2 + 1;
                int padLeft2 = Math.max(0, (maxRingWidth - printWidth2) / 2);
                int padRight2 = Math.max(0, maxRingWidth - printWidth2 - padLeft2);
                System.out.print(rep(" ", padLeft2) + stars2 + rep(" ", padRight2) + "\t\t");
            } else {
                int pad2 = Math.max(0, (maxRingWidth - 1) / 2);
                System.out.print(rep(" ", pad2) + "|" + rep(" ", pad2) + "\t\t");
            }
            // peg 3
            if (pfahl3.size() > level) {
                Ring ring3 = pfahl3.get(level);
                int size3 = ring3.getGroesse();
                String stars3 = rep("*", size3) + ring3.getLabel() + rep("*", size3);
                int printWidth3 = size3 * 2 + 1;
                int padLeft3 = Math.max(0, (maxRingWidth - printWidth3) / 2);
                int padRight3 = Math.max(0, maxRingWidth - printWidth3 - padLeft3);
                System.out.print(rep(" ", padLeft3) + stars3 + rep(" ", padRight3) + "\t\t");
            } else {
                int pad3 = Math.max(0, (maxRingWidth - 1) / 2);
                System.out.print(rep(" ", pad3) + "|" + rep(" ", pad3) + "\t\t");
            }
            System.out.println();
        }

        for (int i = 0; i < 3; i++) {
            String label = "Pfahl " + (i + 1);
            int padLeft = Math.max(0, (maxRingWidth - label.length()) / 2);
            int padRight = Math.max(0, maxRingWidth - label.length() - padLeft);
            System.out.print(rep(" ", padLeft) + label + rep(" ", padRight) + "\t\t");
        }
        System.out.println();
    }

    // recursive simulation of optimal moves
    private static void simulateOptimal(int n, ArrayList<Ring> src, ArrayList<Ring> aux, ArrayList<Ring> dest, int srcNum, int auxNum, int destNum) throws InterruptedException {
        if (n == 0) return;
        simulateOptimal(n - 1, src, dest, aux, srcNum, destNum, auxNum);
        Ring ring = src.remove(src.size() - 1);
        dest.add(ring);
        System.out.println("\n\nBewege Ring " + ring.getLabel() + " von Pfahl " + srcNum + " zu Pfahl " + destNum);
        printTurm();
        Thread.sleep(1000);
        simulateOptimal(n - 1, aux, src, dest, auxNum, srcNum, destNum);
    }
}