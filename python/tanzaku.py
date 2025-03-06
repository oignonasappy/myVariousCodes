# 短冊メーカ
import random

# デコレーション
DECO1 = True
DECO2 = False
# 枠
TOP = '┴'
HOLIZONTAL = 'ー'
VERTICAL = '│'
LEFT_UPPER = '┌'
RIGHT_UPPER = '┐'
LEFT_LOWER = '└'
RIGHT_LOWER = '☆ミ' if DECO1 else '┘'
# 天の川
STAR = ['+','＋','※',',','。','~','〃','…','・','¨','∴','∵',\
        '；',';','：',':','-','/','^','.','ュ']
# 行ごとの字下げ
INDENTATION = 1


wish = []
msg = "七月七日"
cnt = 0
print("すべて全角で入力してください\n空白を入力すると終了します")
# 入力
while msg != "":
  cnt += 1
  print(cnt,"行目 : ",end='')
  msg = input()
  if msg != "": wish += ['　'*(INDENTATION*(cnt-1)) + msg]

# 最も長い行を算出
arr = []
for i, s in enumerate(wish):
  arr += [len(s)]
height = max(arr)

# 偶数行の場合中心の行を空ける
if len(wish)%2 == 0:
  wish.insert(len(wish)//2,'　'*height)

# 入力を縦書きにする
vertWrite = []
for i in range(height):
  col = ""
  for j in range(len(wish)):
    col += '　' if len(wish[len(wish)-1-j]) < i+1 else wish[len(wish)-1-j][i]
  vertWrite += [col]

# 枠
holz = HOLIZONTAL*(len(wish)//2)
upper = LEFT_UPPER + holz + TOP + holz + RIGHT_UPPER
lower = LEFT_LOWER + HOLIZONTAL*((len(wish))//2*2+1) + RIGHT_LOWER

# 天の川
def amanogawa(i):
  if DECO2:
    r1 = random.randint(2,5)
    print('　'*(i//2+r1),end='')
    r1 = random.randint(5,8)
    for j in range(r1):
      r2 = random.randint(0,len(STAR)-1)
      print(STAR[r2],end='')

# 表示
print(upper)
for i in range(height):
  print(VERTICAL,end='')
  print(vertWrite[i],end='')
  print(VERTICAL,end='')
  amanogawa(i)
  print()
print(lower,end='')
for k in range(i,i+2):
  amanogawa(k)
  print('\n',"　"*(len(wish)+2),end='')
