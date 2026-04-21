# Flashcard сургалтын систем

F.CSA311 Программ хангамжийн бүтээлт — Бие даалт №1

## Ажиллуулах

```bash
mvn package -q
java -jar target/flashcard-1.0-SNAPSHOT.jar <cards-file> [options]
```

## Сонголтууд

| Сонголт | Тайлбар | Default |
|---------|---------|---------|
| `--help` | Тусламжийн мэдээлэл | - |
| `--order <random\|worst-first\|recent-mistakes-first>` | Картын дараалал | random |
| `--repetitions <num>` | Нэг картыг хэдэн удаа зөв хариулах | 1 |
| `--invertCards` | Асуулт, хариултыг сольж харуулах | false |

## Жишээ

```bash
# Энгийн
java -jar target/flashcard-1.0-SNAPSHOT.jar cards.txt

# Буруу хариулсан картыг эхэнд харуулах
java -jar target/flashcard-1.0-SNAPSHOT.jar cards.txt --order recent-mistakes-first

# 3 удаа зөв хариулах шаардлагатай
java -jar target/flashcard-1.0-SNAPSHOT.jar cards.txt --repetitions 3

# Асуулт хариултыг сольж харуулах
java -jar target/flashcard-1.0-SNAPSHOT.jar cards.txt --invertCards
```

## cards.txt формат