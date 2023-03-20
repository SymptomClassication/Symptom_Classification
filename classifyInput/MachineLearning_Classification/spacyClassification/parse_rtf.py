import re
import unicodedata
import string
import json


files = ["ACONITUM.rtf", "AGARICUS.rtf", "ARGENTUM NITRICUM.rtf", "ARSENICUM ALBUM.rtf", "BRYONIA.rtf", "CALCAREA .rtf", "CANNABIS .rtf", "CANTHARIS.rtf", "CARBO VEGETABILIS.rtf", "CAUSTICUM.rtf", "CHELIDONIUM MAJUS.rtf", "CONIUM.rtf", "GLONOINE.rtf", "GRAPHITES.rtf", "HYOSCYAMUS.rtf", "IGNATIA.rtf", "KALI BICHROMICUM.rtf", "KALI CARBONICUM.rtf", "LACHESIS.rtf", "LYCOPODIUM.rtf"]
chapters = set()

chapters_id_match = {
    'BACK': [80],
    'INTELLECTUAL': [128],
    'SLEEPINESS': [106],
    'APPETITE': [41],
    'HEAD': [3],
    'EARS' : [24],
    'APPEITE': [41],
    'FOREHEAD': [4],
    'LUMBAR': [82],
    'HEART': [76],
    'UMBILICAL': [51],
    'SHOULDER': [87],
    'FIGNERS': [92],
    'URINE': [62],
    'EYES': [18],
    'CHIN': [31],
    'PUPIL': [22],
    'SIDES': [73],
    'FINGERS': [92],
    'COUGH': [67],
    'THROAT': [118],
    'PARTIALS': [66],
    'THIGH': [96],
    'MAMMAE': [74],
    'LARYNX': [68],
    'UVULA': [120],
    'TONSILES': [120],
    'TONGUE': [35],
    'NECK': [79],
    'ELBOW': [89],
    'URETHRA': [61],
    'EXTREMITIES': [84],
    'PULSE': [77],
    'ERUCTATIONS': [44],
    'RESPIRATION': [70],
    'MENTRUATION': [64],
    'TTEMPLES': [5],
    'LACHRYMATION': [21],
    'EYEBALL': [115],
    'HYPOCHONDREA': [50],
    'ANKLE': [99],
    'DIAHHREA': [57],
    'STOMACH': [48],
    'TOES': [101],
    'SLEEP': [105],
    'HEAT': [110],
    'VERTEX': [6],
    'HYPOCHINDRIA': [50],
    'CONSTIPCAITON': [58],
    'SWALLOWING': [122],
    'HEARING': [25],
    'NAUSEA': [47],
    'CHILLNESS': [109],
    'LIPS': [30],
    'KIDNYES': [60],
    'SLEEPLESSNESS': [107],
    'THRIST': [43],
    'ARMS': [88],
    'FACE': [28],
    'HEARTBURN': [46],
    'JAW': [116],
    'GUMS': [34],
    'MICTURITION': [62],
    'MICTURATION': [62],
    'GIARRHEA': [57],
    'TEETH': [32],
    'LIDS': [20],
    'HYPOGASTRIUM': [53],
    'CONSIPATION': [58],
    'EYE': [18],
    'LEG': [98],
    'MOUTH': [36],
    'EAR': [24],
    'ORBIT': [19],
    'TONSILS': [120],
    'FEMALE': [64],
    'VOMITING': [47],
    'FAUCES': [121],
    'FRONT': [73],
    'FOOT': [100],
    'CHEEKS': [29],
    'HYPOCHONDRIA': [50],
    'DIARRHEA': [57],
    'FOREARM': [90],
    'THIRST': [43],
    'SLEEPNESS': [106],
    'FEVER': [108],
    'MAMMEA': [74],
    'ARM': [88],
    'JAWS': [116],
    'AGGRAVATION': [129],
    'HYPOCHONDIRA': [50],
    'ABDOMEN': [52],
    'HIPS': [95],
    'CONJUCTIVA': [114],
    'FEET': [100],
    'TEMPLES': [5],
    'CONSTIPATION': [58],
    'CONJUNCTIVA': [114],
    'NOSE': [26],
    'ANUS': [55],
    'TIGHS': [96],
    'PHARYNX': [121],
    'PERINEUM': [55],
    'MIND': [124],
    'EHARTBURN': [46],
    'SALIVA': [37],
    'CONSITPATION': [58],
    'SPEECH': [36],
    'DREAMS': [104],
    'MALE': [65],
    'SHOULDERS': [87],
    'HANDS': [92],
    'FAUCEAS': [121],
    'ERUCTATION': [44],
    'RECTUM': [55],
    'KUMBAR': [82],
    'VISION': [23],
    'WRIST': [91],
    'STOOL': [56],
    'SKIN': [103],
    'OCCIPUT': [8],
    'HAND': [92],
    'TRHOAT': [119],
    'SCALP': [12],
    'HIP': [95],
    'SACRAL': [83],
    'ANKLES': [99],
    'BALDDER': [60],
    'KNEE': [97],
    'SWEAT': [112],
    'BLADDER': [60],
    'CHEST': [72],
    'VOICE': [69],
    'LACHRYMAL': [21],
    'DORSAL': [81],
    'GENERALITIES': [102],
    'TASTE': [38],
    'PHARYX': [121],
    'MICTURIUM': [62],
    'CHILINESS': [109],
    'VERTIGO': [1]
}

training_data = []
def create_trainingData():

    with open("trainingData/matchingTrainingData_new.json", "r") as f:
        labels = list(json.load(f))

    with open("trainingData/trainingData_new.json", "r") as f:
        train_data = list(json.load(f))

    count = 0

    for i in range(len(labels)):
        training_data.append({'id': count, 'data': train_data[i], 'labels': labels[i]})
        count += 1

    json.dump(training_data, open("trainingData/database_trainingData.json", "w"))

def read_rtf_file():

    with open("trainingData/matchingTrainingData.json", "r") as f:
        labels = list(json.load(f))

    with open("trainingData/trainingData.json", "r") as f:
        train_data = list(json.load(f))

    print(len(train_data))
    print(len(labels))


    lines = []
    symptoms = {}
    for file in files:
        filename = 'more-data/' + file
        with open(filename, 'r') as f:
            for line in f:
                line = re.sub(r'\\[a-zA-Z]+', '', line)
                line = re.sub(r'\d', '', line)
                line = re.sub(r'\\', ' ', line)
                line = re.sub(r'\[', '', line)
                line = re.sub(r'\]', '', line)
                line = re.sub(r'\'', '', line)
                line = re.sub(r'al', '', line)
                line = re.sub(r'\*', '', line)
                line = re.sub(r'\(', '', line)
                line = re.sub(r'\)', '', line)
                line = re.sub(r'\.', '', line)
                line = re.sub(r',', '', line)
                line = re.sub(r'\\h', '', line)
                line = re.sub(r'b+', '', line)
                line = re.sub(r'_', '', line)
                line = re.sub(r' [a-z] ', '', line)
                line = line.strip()
                line = ''.join(char for char in line if char in string.printable)
                line = re.sub(r'}', '', line)
                line = re.sub(r'{', '', line)
                line = re.sub(r'[\t\s]+', ' ', line)
                if line.strip() == '':
                    continue
                lines.append(line.strip())

        for item in lines:
            r = re.sub(r' ', '', item)
            if all(char.isupper() for char in r):
                if item in chapters_id_match:
                    symptoms[item] = []
                    index = lines.index(item)
                    for i in range(index + 1, len(lines)):
                        k = re.sub(r' ', '', lines[i])
                        if all(char.isupper() for char in k):
                            break
                        else:
                            symptoms[item].append(lines[i])

        for key in symptoms:
            chapters.add(key)

    new_symptoms = {}

    for key in symptoms:
        if key in chapters_id_match:
            new_symptoms[chapters_id_match[key][0]] = symptoms[key]


    train_data = []
    labels = []
    for key in new_symptoms:
        for s in new_symptoms[key]:
            train_data.append(s)
            labels.append(key)

    print(len(train_data))
    print(len(labels))
    json.dump(labels, open("trainingData/matchingTrainingData_new.json", "w"))
    json.dump(train_data, open("trainingData/trainingData_new.json", "w"))



if __name__ == '__main__':
    read_rtf_file()
    create_trainingData()