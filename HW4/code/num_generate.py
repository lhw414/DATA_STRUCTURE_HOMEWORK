import csv
import random

# CSV 파일 경로
csv_file_path = 'rate_data.csv'

# 배열 길이
array_length = 50000

# 최소값과 최대값
min_value = -10000000
max_value = 10000000

# Collision rate와 Sorted rate 단위
rate_unit = 0.0005

# CSV 파일에 저장할 데이터 리스트
data = []

# Collision rate 생성
collision_rate = 0.9
while collision_rate <= 0.9999:
    collision_count = int(array_length * collision_rate)
    collision_nums = [random.randint(min_value, max_value) for _ in range(collision_count)]
    random_nums = [random.randint(min_value, max_value) for _ in range(array_length - collision_count)]
    nums = collision_nums + random_nums
    random.shuffle(nums)
    data.append(('C', collision_rate, nums))
    collision_rate += rate_unit

# Sorted rate 생성
sorted_rate = 0.9
while sorted_rate <= 1.0:
    sorted_count = int(array_length * sorted_rate)
    sorted_nums = sorted([random.randint(min_value, max_value) for _ in range(sorted_count)])
    random_nums = [random.randint(min_value, max_value) for _ in range(array_length - sorted_count)]
    nums = sorted_nums + random_nums
    random.shuffle(nums)
    data.append(('S', sorted_rate, nums))
    sorted_rate += rate_unit

# CSV 파일에 데이터 저장
with open(csv_file_path, 'w', newline='') as file:
    writer = csv.writer(file)
    writer.writerow(['Type', 'Rate', 'Numbers'])
    for item in data:
        writer.writerow(item)

print(f"CSV 파일이 생성되었습니다: {csv_file_path}")
