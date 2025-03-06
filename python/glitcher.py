import numpy
import wave
##import struct
import matplotlib.pyplot

# 音を切り取って繰り返す
# グリッチ音を生成
# [イメージ] ABCDEF -> AACCEE  ABCDEFGHI -> AAADDDGGG

# ファイル読み込み
file_name = input("[読み込み]:ファイル名(拡張子を含む)\n> ")
directory = "glitcher\\" + file_name  # ディレクトリ(相対,自身のglicherフォルダーの中)
wav_import = wave.open(directory, 'r')

# wavの情報取得
ch = wav_import.getnchannels()
width = wav_import.getsampwidth()
fr = wav_import.getframerate()
fn = wav_import.getnframes()

# wavの情報表示
print("Channel:     ", ch)  # 1:モノラル, 2:ステレオ
print("Sample width:", width)  # 1サンプルのバイト数(通常、2B = 16b)
print("Frame Rate:  ", fr)  # 秒間サンプル数
print("Frame num:   ", fn)  # 合計サンプル数
print("Params:      ", wav_import.getparams())  # すべてのパラメータ
print("Total time:  ", 1.0 * fn / fr)  # 秒

# waveの実データ取得
data = wav_import.readframes(fn)
wav_import.close()
# byte列からint列に
wav_imp_buf = numpy.frombuffer(data, dtype=numpy.int16)


# データ描写
def plot_show(fr,fn,ch,wavdata):
    if fn != len(wavdata)//2:  # 本来のデータの長さと(なぜか)異なったとき
        print("irregular")
        size = float(len(wavdata)//2)
    else:
        size = float(fn)  # 波形サイズ
    x = numpy.arange(0, size/fr, 1.0/fr)

    # plot
    plot_type = 0  # 表示の種類 0~
    if plot_type == 1:  # チャンネル毎に表示
        fig, axs = matplotlib.pyplot.subplots(ch,1, figsize=(16, 8))
        for i in range(ch):
            if i % 2 == 0:
                cl = 'b'
            else:
                cl = 'r'
            axs[i].plot(x,wavdata[i::ch], lw=0.3, color=cl, label="ch"+str(i+1))
            axs[i].legend(loc='upper right')
    else:  # 同一のグラフに表示
        fig, ax = matplotlib.pyplot.subplots(figsize=(16, 8))
        # 色は交互に
        for i in range(ch):
            if i % 2 == 0:
                cl = 'b'
            else:
                cl = 'r'
            # wavのデータはチャンネル毎に交互になっている
            ax.plot(x,wavdata[i::ch], lw=0.2, color=cl, label="ch"+str(i+1))
            # 線のチャンネル名を表示
            ax.legend(loc='upper right')
    matplotlib.pyplot.show()


if input("実行前のデータを描写しますか？[y or n]\n> ") == "y":
    plot_show(fr,fn,ch,wav_imp_buf)

# glicher  <<設定はココ>>
block = 2400  # 一ブロックのサンプル数
repeat = 4  # ブロックの繰り返し数

length = fn - (fn % (block * repeat)) + (block * repeat)  # 完成後のサンプル数

# リストに追加、最後に結合
wave_list = []
for i in range(length // (block * repeat)):  # ←※僅かにデータ長に誤りがある [要修正]
    # データ長はチャンネル数に比例する
    for j in range(repeat):
        wave_list.append(wav_imp_buf[i*(block*repeat)*ch:(i*(block*repeat)+block)*ch])
# 結合
joinedWave = numpy.empty(0, dtype=numpy.int16)
joinedWave = numpy.concatenate(wave_list[:])

if input("実行後のデータを描写しますか？[y or n]\n> ") == "y":
    # 不具合放置中 描画できるかは運次第
    ##print(fn,fn*2,length,length*2,len(joinedWave))
    plot_show(fr,length,ch,joinedWave)

# パラメータの書き込み
wave_fname = file_name[0:-4] + "_glitcher.wav"
with wave.open("glitcher\\"+wave_fname, 'w') as wave_write:  # ディレクトリ保存
    params = (ch, width, fr, length, 'NONE', 'not compressed')
    wave_write.setparams(params)
    wave_write.writeframes(joinedWave)
    wave_write.close()

print("終了しました。")
