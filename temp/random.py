with open("numbers.txt", "w") as file:
    for i in range(1, 1001):
        file.write(f"{i}\n")