
# Lab 10 — Data Flow Testing (Definition-Use Pairs)

## Оюутны мэдээлэл

- **Нэр:** Э. Мөнх-Очир
- **Оюутны код:** B232270022
- **Хичээл:** F.CSA313 — Программ хангамжийн чанарын баталгаа ба тест


## Орчны мэдээлэл

### Үйлдлийн систем

```text
OS name: mac os x
OS version: 27.0.1
Architecture: aarch64
Platform: macOS
```

### Java — java -version

Команд:

```bash
java -version
```

Гаралт:

```text
openjdk version "25.0.2" 2026-01-20
OpenJDK Runtime Environment Homebrew (build 25.0.2)
OpenJDK 64-Bit Server VM Homebrew (build 25.0.2, mixed mode, sharing)
```

### Maven — mvn -version

Команд:

```bash
mvn -version
```

Гаралт:

```text
Apache Maven 3.9.12 (848fbb4bf2d427b72bdb2471c22fced7ebd9a7a1)
Maven home: /opt/homebrew/Cellar/maven/3.9.12/libexec
Java version: 25.0.2, vendor: Homebrew, runtime: /opt/homebrew/Cellar/openjdk/25.0.2/libexec/openjdk.jdk/Contents/Home
Default locale: en_US, platform encoding: UTF-8
OS name: "mac os x", version: "27.0.1", arch: "aarch64", family: "mac"
```

## Part A — gcd() Definition-Use Analysis

### A1. DU Pairs Table

| Variable | Def line | Use line | Use type | Def-clear path | Test ID |
|---|---:|---:|---|---|---|
| x | 13 | 16 | C-use | 13→15(T)→16 | testGcdWithLoop |
| x | 13 | 20 | C-use | 13→15(F)→20 | testGcdWithoutLoop |
| x | 17 | 16 | C-use | 17→18→15(T)→16 | testGcdWithLoop |
| x | 17 | 20 | C-use | 17→18→15(F)→20 | testGcdWithLoop |
| y | 13 | 15 | P-use (T) | 13→15(T) | testGcdWithLoop |
| y | 13 | 15 | P-use (F) | 13→15(F) | testGcdWithoutLoop |
| y | 13 | 16 | C-use | 13→15(T)→16 | testGcdWithLoop |
| y | 13 | 17 | C-use | 13→15(T)→16→17 | testGcdWithLoop |
| y | 18 | 15 | P-use (T) | 18→15(T) | testGcdWithLoop |
| y | 18 | 15 | P-use (F) | 18→15(F) | testGcdWithLoop |
| y | 18 | 16 | C-use | 18→15(T)→16 | testGcdWithLoop |
| y | 18 | 17 | C-use | 18→15(T)→16→17 | testGcdWithLoop |
| tmp | 16 | 18 | C-use | 16→17→18 | testGcdWithLoop |

### A2. Test Results

Command:

```bash
mvn clean verify
```

Results:

- Tests run: 2
- Failures: 0
- Errors: 0
- Skipped: 0
- BUILD SUCCESS

### A3. Test Explanation

**testGcdWithoutLoop()**

Input: gcd(7, 0)

Expected output: 7

The loop does not execute. This test covers the initial definitions of x and y reaching the return and loop condition without being redefined.

**testGcdWithLoop()**

Input: gcd(12, 8)

Expected output: 4

The Euclidean algorithm executes two iterations. This test covers the loop-related definition-use pairs, including tmp.

The gcd(12, 8) execution performs two iterations because:

1. 12 % 8 = 4
2. 8 % 4 = 0

The final GCD is 4.
