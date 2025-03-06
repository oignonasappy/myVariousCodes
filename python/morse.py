import numpy as np
import wave
import struct

# [key]alphabet : [value]morse
ALP_TO_MORSE = {'a':'01','b':'1000','c':'1010','d':'100','e':'0','f':'0010',\
                'g':'110','h':'0000','i':'00','j':'0111','k':'101','l':'0100',\
                'm':'11','n':'10','o':'111','p':'0110','q':'1101','r':'010',\
                's':'000','t':'1','u':'001','v':'0001','w':'011','x':'1001',\
                'y':'1011','z':'1100','1':'01111','2':'00111','3':'00011',\
                '4':'00001','5':'00000','6':'10000','7':'11000','8':'11100',\
                '9':'11110','0':'11111','?':'001100','/':'10010','-':'100001',\
                '_':'001101','.':'010101',',':'110011',':':'111000',\
                '(':'10110',')':'101101','"':'010010',"'":'011110'}
# [key]morse : [value]alphabet
MORSE_TO_ALP = {'01':'a','1000':'b','1010':'c','100':'d','0':'e','0010':'f',\
                '110':'g','0000':'h','00':'i','0111':'j','101':'k','0100':'l',\
                '11':'m','10':'n','111':'o','0110':'p','1101':'q','010':'r',\
                '000':'s','1':'t','001':'u','0001':'v','011':'w','1001':'x',\
                '1011':'y','1100':'z','01111':'1','00111':'2','00011':'3',\
                '00001':'4','00000':'5','10000':'6','11000':'7','11100':'8',\
                '11110':'9','11111':'0','001100':'?','10010':'/','100001':'-',\
                '001101':'_','010101':'.','110011':',','111000':':',\
                '10110':'(','101101':')','010010':'"','011110':"'",}
# モールス符号の字間
SPACE = ','


# wavファイルを生成
def Wave(ans):
  length=[]
  i=-1
  # 一文字ずつ音の長さを決める
  for _,s in enumerate(ans):
    match s:
      case '0':
        length.append([1])  # on
        i+=1
        length[i].append(1)  # short 1
        length.append([0])  # off
        i+=1
        length[i].append(1)  # inter 1
      case '1':
        length.append([1])  # on
        i+=1
        length[i].append(3)  # long 3
        length.append([0])  # off
        i+=1
        length[i].append(1)  # inter 1
      case ',':
        length.append([0])  # off
        i+=1
        length[i].append(2)  # char space 2(3)
      case _:
        length.append([0])  # off
        i+=1
        length[i].append(2)  # word space 2(7)

  A = 1 # 振幅
  fs = 44100 # サンプリングレート
  f0 = 784 # 基本周波数
  sec = 0.05 # 短符1の再生時間
  waveList = list()
  for i,s in enumerate(length):
    if s[0] == 0:
      # 無音を生成しリストに追加
      waveList.append(np.zeros(int(fs*sec*s[1]), dtype=np.int16))
    elif s[0] == 1:
      # 連続した配列
      nframe = np.arange(0, int(fs*sec*s[1]))
      # sin波を生成
      sin_wave = A*np.sin(nframe*2*np.pi*f0/fs)
      # 16bit符号付き整数に変換
      sin_wave = np.array(sin_wave * (2**15-1)).astype(np.int16)
      # リストに追加
      waveList.append(sin_wave.copy())

  # 音をつなげる
  joinedWave = np.empty(0, dtype=np.int16)
  joinedWave = np.concatenate(waveList[:])

  # ファイル名
  name = input("↓file name↓\n")
  wave_fname = "morse.wav" if name == "" else name+".wav"

  # バイナリに変換
  binary_sin_wave = struct.pack("h"*len(joinedWave), *joinedWave)
  wave_write = wave.Wave_write(wave_fname)
  params = (1, 2, fs, len(joinedWave), 'NONE', 'not compressed')

  # 書き込み
  wave_write.setparams(params)
  wave_write.writeframes(binary_sin_wave)
  wave_write.close()

  print("Complete!")


# alphabet to morse
def AtoM():
  print("\nALPHABET TO MORSE")
  errorDetection = 0
  ctnsErr = True
  print("↓Enter text {only lowercase are valid}↓")
  msg = input()
  ans=""
  for i,s in enumerate(msg):
    if s in ALP_TO_MORSE:
      ans += ALP_TO_MORSE[s]
      ctnsErr = True
    else:
      if ctnsErr:
        ans += "`"
        errorDetection+=1
        ctnsErr = False
    ans += SPACE
  print("Exception Detection :",errorDetection,"→ [ ` ]\n\n↓Here is answer↓")
  print(ans)
  isGenWave = input("\n[yes:1]Do you wanna generate .wav file? → ")
  if isGenWave == "1": Wave(ans)


# morse to alphabet
def MtoA():
  print("\nMORSE TO ALPHABET")
  errorDetection = 0
  ctnsErr = True
  print("↓Enter binary [Any one character between letters]↓")
  msg = input()
  if msg[len(msg)-1] == '0' or msg[len(msg)-1] == '1': msg += "."
  ans=""
  let=""
  i=0
  for i,s in enumerate(msg):
    if s == '0' or s == '1':
      let += s
    else:
      if let in MORSE_TO_ALP:
        ans += MORSE_TO_ALP[let]
        ctnsErr = True
      else:
        if ctnsErr:
          ans += "`"
          errorDetection+=1
          ctnsErr = False
      let=""
  print("Exception Detection :",errorDetection,"→ [ ` ]\n\n↓Here is answer↓")
  print(ans)


def GenWav():
  print(".wav GENERATE")
  print("↓Enter binary [Any one character between letters]↓")
  msg = input()
  Wave(msg)


# ALP_TO_MORSEのキー,値の反転
def ATMOutputRev():
  for k,v in ALP_TO_MORSE.items():
    print("'"+v+"':"+"'"+k+"',",end='')


# main ここから
print("------------------------")
print("[available]")
print("lowercase alphabet")
print("numeric")
print("?/-_.,:()\"'") # '!', '`', ' ' does not exist
print("------------------------")

option = input("--1:ALPHABET TO MORSE\n--2:MORSE TO ALPHABET\n"\
               "--3:.wav GENERATE\n→ ")
match option:
  case '2': MtoA()
  case '3': GenWav()
  case 'A': ATMOutputRev()
  case _: AtoM()
