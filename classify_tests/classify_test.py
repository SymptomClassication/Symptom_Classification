import PyPDF2 as pdfReader
import re
import json


def extract_symptoms(pdf_file: str):
    symptoms = []
    pdf_content = []

    pdf_obj = open(pdf_file, 'rb')
    chapters_pdf = pdfReader.PdfFileReader(pdf_obj)

    for i in range(chapters_pdf.getNumPages()):
        page = chapters_pdf.getPage(i)
        page_content = page.extractText()
        pdf_content.append(page_content)

    all_lines = "\n".join(pdf_content).splitlines()

    for line in all_lines:
        if line[0].isdigit() and "Allen" not in line:
            removeNum = re.sub(r"[0-9]", "", line)
            removeDot = re.sub(r"\.", "", removeNum)
            removeStar = re.sub(r"\*", "", removeDot)
            removeBracket = re.sub(r"\[*\]*", "", removeStar)
            removeParenthesis = re.sub(r"\((.*?)\)", "", removeBracket)
            # removeSignleCharacter = re.sub(r"\s[abcdefg]*\s", "", removeParenthesis)
            removeComma = re.sub(r",", "", removeParenthesis)
            removeSemiColon = re.sub(r";", "", removeComma)
            if line[0].isalpha():
                previous = symptoms.pop()
                full_line = previous + " " + line
                symptoms.append(full_line)
            symptoms.append(removeSemiColon.strip())

    pdf_obj.close()

    for symptom in symptoms:
        print(symptom)

    return symptoms


def classify(chapters, subchapters, subtitles, symptoms):
    classified_symptoms = []

    for symptom in symptoms:
        main_chapters = set()
        symptom_subtitles = []
        symptom_subchapters = []
        chapter_indexes = set()
        different_chapters = True
        #  case 1: chapter/subtitle found
        for s_list in chapters:
            for chapter in s_list["titles"]:
                if re.search(chapter, symptom, re.IGNORECASE):
                    if chapter == "inner" or chapter == "outer" or chapter == "internal" or chapter == "external":
                        if s_list["titles"][0] not in symptom:
                            continue
                    main_chapters.add(chapter)
                    chapter_indexes.add(s_list["chapterId"])
        if len(main_chapters) == 0:
            for s_list in subtitles:
                for subtitle in s_list["subtitles"]:
                    if re.search(subtitle, symptom, re.IGNORECASE):
                        symptom_subtitles.append(subtitle)
                        chapter_indexes.add(s_list["chapterId"])
                        chapter_names = findChapter(chapter_indexes, chapters)
                        if chapter_names:
                            for name in chapter_names:
                                main_chapters.add(name)
        if len(main_chapters) >= 2:
            different_chapters = True
        else:
            for s_list in subchapters:
                for subchapter in s_list["subchapters"]:
                    if re.search(subchapter, symptom, re.IGNORECASE):
                        symptom_subchapters.append(subchapter)
                        chapter_indexes.add(s_list["chapterId"])
                        if len(main_chapters) == 0:
                            chapter_names = findChapter(chapter_indexes, chapters)
                            if chapter_names:
                                for name in chapter_names:
                                    main_chapters.add(name)

        if len(chapter_indexes) != 0 and len(symptom_subchapters) == 0:
            for s_list in subchapters:
                if s_list["chapterId"] == chapter_indexes:
                    if "General" in s_list["subchapters"]:
                        symptom_subchapters.append("General")
        if len(main_chapters) == 0:
            main_chapters.add("unknown")

        if len(symptom_subchapters) > 0:
            main_chapters = match_subchapters(symptom_subchapters, main_chapters, subchapters, chapters)

        classified_symptoms.append(
            {"symptom": symptom, "chapter": list(main_chapters), "subchapter": symptom_subchapters,
             "subtitle": symptom_subtitles})

    return classified_symptoms


def findChapter(chapter_index, chapters):
    for index in chapter_index:
        for chapter in chapters:
            if chapter["chapterId"] == index:
                return chapter["titles"]


def match_subchapters(subchapters, chapters, subchapters_dict, chapters_dict):
    subchapters_set = subchapters
    chapters_set = list(chapters)
    ids = []

    for s_list in subchapters_dict:
        for subchapter in s_list["subchapters"]:
            for subchapter_set in subchapters_set:
                if re.search(subchapter, subchapter_set, re.IGNORECASE):
                    ids.append(s_list["chapterId"])
    for c_list in chapters_dict:
        if c_list["chapterId"] in ids:
            for title in c_list["titles"]:
                if title not in chapters_set:
                    chapters_set.append(title)

    return set(chapters_set)


def pipeline(input_data="./input/Symptome_Test.pdf"):
    symptoms = extract_symptoms(input_data)

    with open("./input/chapters.json", "r") as f:
        chapters = json.load(f)
    with open("./input/subchapters.json", "r") as f:
        subchapters = json.load(f)
    with open("./input/subtitles.json", "r") as f:
        subtitles = json.load(f)

    classified_symptoms = classify(chapters, subchapters, subtitles, symptoms)

    for symptom in classified_symptoms:
        print(symptom)

    with open("./output_json_files/classified_test.json", "w") as outfile:
        json.dump(classified_symptoms, outfile)


if __name__ == "__main__":
    pipeline()
