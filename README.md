# Lab 10 — Data Flow Testing: Definition-Use Pairs

**Хичээл:** F.CSA313 — Программ хангамжийн чанарын баталгаа ба тест  
**Оюутны нэр:** Э. Мөнх-Очир  
**Оюутны код:** B232270022  
**Сэдэв:** Өгөгдлийн урсгалын тест — Definition-Use Pairs

---

# 1. Орчны мэдээлэл

## 1.1. Үйлдлийн систем

```text
OS name: mac os x
OS version: 27.0.1
Architecture: aarch64
Platform: macOS
```

## 1.2. Java орчин

**Команд:**

```bash
java -version
```

**Гаралт:**

```text
openjdk version "25.0.2" 2026-01-20
OpenJDK Runtime Environment Homebrew (build 25.0.2)
OpenJDK 64-Bit Server VM Homebrew (build 25.0.2, mixed mode, sharing)
```

## 1.3. Maven орчин

**Команд:**

```bash
mvn -version
```

**Гаралт:**

```text
Apache Maven 3.9.12 (848fbb4bf2d427b72bdb2471c22fced7ebd9a7a1)
Maven home: /opt/homebrew/Cellar/maven/3.9.12/libexec
Java version: 25.0.2, vendor: Homebrew, runtime: /opt/homebrew/Cellar/openjdk/25.0.2/libexec/openjdk.jdk/Contents/Home
Default locale: en_US, platform encoding: UTF-8
OS name: "mac os x", version: "27.0.1", arch: "aarch64", family: "mac"
```

---

# 2. Part A — gcd() Definition-Use Analysis

## 2.1. Зорилго

`gcd(x, y)` функцийн хувьсагч бүрийн Definition (Def), Computational Use (C-use), Predicate Use (P-use) цэгүүдийг тодорхойлж, Def-clear замтай DU хосуудыг гаргасан.

## 2.2. DU Pairs хүснэгт

| Variable | Def line | Use line | Use type | Def-clear path | Test ID | Covered |
|---|---:|---:|---|---|---|---|
| x | 13 | 16 | C-use | 13→15(T)→16 | testGcdWithLoop | Yes |
| x | 13 | 20 | C-use | 13→15(F)→20 | testGcdWithoutLoop | Yes |
| x | 17 | 16 | C-use | 17→18→15(T)→16 | testGcdWithLoop | Yes |
| x | 17 | 20 | C-use | 17→18→15(F)→20 | testGcdWithLoop | Yes |
| y | 13 | 15 | P-use (T) | 13→15(T) | testGcdWithLoop | Yes |
| y | 13 | 15 | P-use (F) | 13→15(F) | testGcdWithoutLoop | Yes |
| y | 13 | 16 | C-use | 13→15(T)→16 | testGcdWithLoop | Yes |
| y | 13 | 17 | C-use | 13→15(T)→16→17 | testGcdWithLoop | Yes |
| y | 18 | 15 | P-use (T) | 18→15(T) | testGcdWithLoop | Yes |
| y | 18 | 15 | P-use (F) | 18→15(F) | testGcdWithLoop | Yes |
| y | 18 | 16 | C-use | 18→15(T)→16 | testGcdWithLoop | Yes |
| y | 18 | 17 | C-use | 18→15(T)→16→17 | testGcdWithLoop | Yes |
| tmp | 16 | 18 | C-use | 16→17→18 | testGcdWithLoop | Yes |

## 2.3. GCD тестүүд

Тестийн файл:

`src/test/java/mn/edu/must/sqat/GcdTest.java`

### testGcdWithoutLoop()

```java
assertEquals(7, DataFlowTargets.gcd(7, 0));
```

Хүлээгдэж буй үр дүн: `7`

Энэ тестэд `y = 0` тул while давталт ажиллахгүй. Анхны `x` утга 20-р мөрийн return-д хүрнэ.

### testGcdWithLoop()

```java
assertEquals(4, DataFlowTargets.gcd(12, 8));
```

Хүлээгдэж буй үр дүн: `4`

Евклидийн алгоритм хоёр удаа давталт хийнэ:

1. 12 % 8 = 4
2. 8 % 4 = 0

Энэ тестээр давталтын үеийн Def-Use хосуудыг хамруулна.

## 2.4. GCD тестийн үр дүн

Команд:

```bash
mvn clean verify
```

Үр дүн:

```text
Tests run: 2, Failures: 0, Errors: 0, Skipped: 0

BUILD SUCCESS
```

---

# 3. Part B — bonus() Definition-Use Analysis

## 3.1. Функцийн тайлбар

`bonus(int[] attendance, int threshold)` функц нь ирцийн мэдээлэлд үндэслэн урамшууллын оноо тооцно.

Үндсэн дүрэм:

- Ирсэн өдрийн тоо `threshold`-ээс их эсвэл тэнцүү бол 5 оноо өгнө.
- Дараалсан ирцийн хамгийн урт цуваа 5 буюу түүнээс их бол нэмэлт 3 оноо өгнө.
- `attendance == null` эсвэл `threshold < 0` бол `IllegalArgumentException` үүсгэнэ.

## 3.2. B1 — Control Flow Graph

`bonus()` функцийн Control Flow Graph-ийг Graphviz ашиглан үүсгэсэн.

CFG зураг:

![Control Flow Graph](cfg-bonus.png)

Файлын нэр: `cfg-bonus.png`

CFG-д дараах урсгалуудыг дүрсэлсэн:

- Оролтын шалгалт
- Initialization
- For-each давталт
- `a > 0` нөхцөл
- `streak > best` нөхцөл
- `streak = 0` дахин тодорхойлолт
- Оноо тооцоолох нөхцөлүүд
- Return болон Exception урсгал

For-each давталтын 38-р мөрийн нөхцөлийг `attendance`-ийн P-use, дараагийн элементийг `a` хувьсагчид оноох үйлдлийг `a`-ийн Def гэж үзсэн.

## 3.3. B2 — Definition / Use жагсаалт

Def — хувьсагчид шинэ утга оноох үйлдэл.

C-use — тооцоолол болон утга ашиглах үйлдэл.

P-use — нөхцөл шалгах үед хувьсагч ашиглах үйлдэл.

| Line | Variable | Def | C-use | P-use |
|---:|---|---|---|---|
| 31 | attendance | Parameter Def | — | — |
| 31 | threshold | Parameter Def | — | — |
| 32 | attendance | — | — | attendance == null |
| 32 | threshold | — | — | threshold < 0 |
| 35 | present | Def | — | — |
| 36 | streak | Def | — | — |
| 37 | best | Def | — | — |
| 38 | attendance | — | — | Next element exists |
| 38 | a | Def | — | — |
| 39 | a | — | — | a > 0 |
| 40 | present | Def | present | — |
| 41 | streak | Def | streak | — |
| 42 | streak | — | — | streak > best |
| 42 | best | — | — | streak > best |
| 43 | streak | — | streak | — |
| 43 | best | Def | — | — |
| 46 | streak | Def | — | — |
| 49 | score | Def | — | — |
| 50 | present | — | — | present >= threshold |
| 50 | threshold | — | — | present >= threshold |
| 51 | score | Def | — | — |
| 53 | best | — | — | best >= 5 |
| 54 | score | Def | score | — |
| 56 | score | — | score | — |

### Хувьсагч тус бүрийн Def/Use

**present**

- Def: 35, 40
- C-use: 40
- P-use: 50

40-р мөрөнд өмнөх утгыг ашиглан present хувьсагчийн утгыг нэгээр нэмэгдүүлнэ.

**streak**

- Def: 36, 41, 46
- C-use: 41, 43
- P-use: 42

46-р мөрөнд `streak = 0` хийгдэхэд өмнөх Def хүчингүй болно.

**best**

- Def: 37, 43
- C-use: 43
- P-use: 42, 53

**score**

- Def: 49, 51, 54
- C-use: 54, 56
- P-use: байхгүй.

**a**

- Def: 38
- C-use: байхгүй.
- P-use: 39

**attendance**

- Def: 31 (parameter)
- C-use: байхгүй.
- P-use: 32, 38

**threshold**

- Def: 31 (parameter)
- C-use: байхгүй.
- P-use: 32, 50

## 3.4. Kill analysis

46-р мөр:

```java
streak = 0;
```

Энэ мөр нь `streak` хувьсагчийн өмнөх тодорхойлолтыг хүчингүй болгоно.

Жишээ:

```text
36: streak = 0
41: streak = streak + 1
46: streak = 0
```

46-р мөрөөр дамжсан тохиолдолд 41-р мөрийн Def нь шинэ Def-ээр kill хийгдэнэ.

Энэ нь DU хосын Def-clear замыг тодорхойлоход чухал ач холбогдолтой.

## 3.5. B3 — DU Pairs хүснэгт

Энэ хэсэгт `bonus()` функцийн бүх боломжтой Def-Use хосыг тодорхойлно.

Хүснэгтийн баганууд:

- Variable
- Def line
- Use line
- C-use / P-use
- Def-clear path
- Test ID
- Covered

**Төлөв:** DU хосуудын бүрэн анализ болон боломжгүй хосуудын үндэслэлийг боловсруулж дууссаны дараа хүснэгтийг бөглөнө.

Kill-ээр тасарсан Def-Use хосыг боломжтой DU хос гэж тооцохгүй.

## 3.6. B4 — All-Defs болон All-DU-Pairs тестүүд

Тестийн файл:

`src/test/java/mn/edu/must/sqat/BonusTest.java`

Энэ хэсэг хоёр тусдаа шаттай.

### Stage 1 — All-Defs Coverage

Хувьсагч бүрийн Def-ийг дор хаяж нэг Use-д хүргэх хамгийн цөөн тестийг тодорхойлно.

Үндэслэл:

- Def бүрийг шалгах шаардлагатай.
- Score-ийн өөр өөр Def-үүд нэг гүйцэтгэлд бүгд Use-д хүрч чадах эсэхийг шинжилнэ.
- Тестийн хамгийн бага тоог үндэслэнэ.

Үр дүн:

- Тестийн тоо: Хэмжилтийн дараа бөглөнө.
- Maven output: `results/mvn-verify-alldefs.txt`
- JaCoCo CSV: `results/jacoco-alldefs.csv`

### Stage 2 — All-DU-Pairs Coverage

All-Defs тестүүд дээр нэмэлт тест нэмж, бүх боломжтой DU хосыг хамруулна.

Үр дүн:

- Тестийн тоо: Хэмжилтийн дараа бөглөнө.
- Бүх DU хосын Test ID баганыг бөглөнө.
- Maven verify амжилттай байх шаардлагатай.

All-Defs болон All-DU-Pairs тестүүдийг тусдаа Git commit-оор хадгална.

## 3.7. B5 — JaCoCo Coverage Analysis

JaCoCo нь branch болон line coverage хэмжинэ.

Гэхдээ JaCoCo branch coverage нь DU-pair coverage-ийг шууд хэмждэггүй.

Эцсийн шаардлага:

- BRANCH_MISSED = 0
- BRANCH_COVERED = 16
- LINE_MISSED = 1
- LINE_MISSED = 1 байх нь зөвшөөрөгдөнө. Энэ нь 7-р мөрийн Java далд constructor юм.

Үр дүн:

- `results/jacoco-alldefs.csv`
- `results/jacoco-final.csv`

All-Defs болон All-DU-Pairs хамралтын JaCoCo үр дүнг харьцуулж, branch coverage болон DU coverage-ийн ялгааг тайлбарлана.

---

# 4. Part C — Fault Seeding

Энэ хэсэгт эх код дээр зориудаар гурван мутаци хийж, тестүүд алдааг илрүүлж чадаж байгаа эсэхийг шалгана.

Мутаци бүрийг нэг нэгээр нь хийж, дараа нь `git restore` ашиглан эх кодыг буцаана.

## 4.1. Mutation M1 — Missing Update

41-р мөрийн дараах үйлдлийг устгана:

```java
streak = streak + 1;
```

Төрөл: Missing update.

Шалгах команд:

```bash
mvn test 2>&1 | tee results/mvn-test-M1.txt
```

Үр дүн: Туршилтын дараа бөглөнө.

## 4.2. Mutation M2 — Wrong Variable

Дараах кодыг:

```java
if (streak > best) {
    best = streak;
}
```

Ингэж өөрчилнө:

```java
if (streak > best) {
    best = present;
}
```

Төрөл: Wrong variable.

Шалгах команд:

```bash
mvn test 2>&1 | tee results/mvn-test-M2.txt
```

Хэрэв M2 амьд үлдвэл түүнийг унагах нэмэлт тест бичиж, дараах гаралтыг хадгална:

```bash
mvn test 2>&1 | tee results/mvn-test-M2-kill.txt
```

## 4.3. Mutation M3 — Dead Definition

Давталтын дараа дараах кодыг нэмнэ:

```java
int total = present + best;
```

Төрөл: Dead definition.

Шалгах команд:

```bash
mvn test 2>&1 | tee results/mvn-test-M3.txt
```

Энэ мутаци нь `total` хувьсагчийг цаашид ашигладаггүй тул ердийн JUnit тестээр илрэхгүй байх ёстой.

IDE-ийн static analysis анхааруулгыг тэмдэглэнэ.

## 4.4. Fault Seeding үр дүн

| Mutation | Төрөл | Тестийн үр дүн | Дүгнэлт |
|---|---|---|---|
| M1 | Missing update | Туршилтын дараа бөглөнө | Хүлээгдэж байна |
| M2 | Wrong variable | Туршилтын дараа бөглөнө | Хүлээгдэж байна |
| M3 | Dead definition | Туршилтын дараа бөглөнө | Хүлээгдэж байна |

Мутаци бүрийн дараа эх кодыг буцаана:

```bash
git restore src/main/java/mn/edu/must/sqat/DataFlowTargets.java
```

---

# 5. Эцсийн Maven болон JaCoCo үр дүн

Эцсийн кодыг сэргээсний дараа:

```bash
mvn verify 2>&1 | tee results/mvn-verify-final.txt
```

JaCoCo CSV-г хадгалах:

```bash
cp target/site/jacoco/jacoco.csv results/jacoco-final.csv
```

Эцсийн шалгалтаар бүх тест амжилттай ажиллаж, BRANCH_MISSED = 0 байх шаардлагатай.

---

# 6. Дүгнэлт

Энэ лабораторийн ажлаар өгөгдлийн урсгалын анализын үндсэн ойлголтууд болох Definition, Computational Use, Predicate Use болон Def-clear path-ийг судалсан.

GCD функц дээр хувьсагч бүрийн DU хосыг тодорхойлж, JUnit тестүүдээр шалгасан.

Bonus функц дээр Control Flow Graph байгуулж, хувьсагч бүрийн Def болон Use цэгүүдийг тодорхойлсон.

Мөн All-Defs болон All-DU-Pairs хамралтын ялгааг туршилтаар судална.

Fault Seeding хэсэгт зориудаар алдаа үүсгэж, тестүүд тухайн алдааг илрүүлж чадаж байгаа эсэхийг шалгана.

JaCoCo branch coverage болон DU-pair coverage нь өөр өөр хэмжүүр болохыг үр дүнгээр харьцуулан дүгнэнэ.

---

# 7. GitHub Repository

Repository: `https://github.com/Hirito572/lab10-dataflow`

Ажлын явцын commit-ууд:

1. Starter project
2. GCD DU-pair tests
3. Bonus All-Defs tests
4. Bonus All-DU-Pairs tests
5. Fault Seeding results
6. Final README and results

---

# 8. AI ашигласан тухай

Энэхүү лабораторийн ажлыг гүйцэтгэх явцад ChatGPT AI туслахыг ашигласан.

AI туслахыг дараах зорилгоор ашигласан:

- Definition-Use Pair ойлголтыг тайлбарлуулах.
- Control Flow Graph боловсруулахад туслуулах.
- Def/Use жагсаалт болон DU хүснэгтийн бүтцийг боловсруулах.
- JUnit тестийн бүтэц боловсруулах.
- Maven болон JaCoCo үр дүнг тайлбарлуулах.

Кодыг өөрийн компьютер дээр ажиллуулж, тестийн үр дүнг шалган баталгаажуулсан.