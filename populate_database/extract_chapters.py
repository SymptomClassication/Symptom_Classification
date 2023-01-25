import PyPDF2 as pdfReader
import re
import json


def extract_chapters(pdf_file: str):
    chapters = []
    pdf_content = []

    pdf_obj = open(pdf_file, 'rb')
    chapters_pdf = pdfReader.PdfFileReader(pdf_obj)

    for i in range(chapters_pdf.getNumPages()):
        if i == 0:
            continue
        page = chapters_pdf.getPage(i)
        page_content = page.extractText()
        pdf_content.append(page_content)

    all_lines = "\n".join(pdf_content).splitlines()

    for line in all_lines:
        single_chapter = []
        if line[0].isdigit():
            single_chapter.append(line)
            i = 1
            while not all_lines[all_lines.index(line) + i][0].isdigit():
                single_chapter.append(all_lines[all_lines.index(line) + i])
                if all_lines.index(line) + i == len(all_lines) - 1:
                    break
                i += 1
        if single_chapter:
            chapters.append(single_chapter)

    pdf_obj.close()
    return chapters


def parse_chapters(chapters_list: list):
    parsed_chapter_titles = []
    parsed_chapter_subtitles = []
    parsed_chapter_subchapters = []

    for chapter in chapters_list:
        chapter_id = ""
        chapter_title = []
        chapter_subtitle = []
        subchapters = []
        i = 0
        while i in range(len(chapter)):
            if i == 0:  # get index and title
                chapter_id = re.search(r"(\d*)", chapter[i])
                chapter_titles = re.search(r"[^.]*$", chapter[i])
                for title in chapter_titles.group(0).split(","):
                    if "and" in title:  # case where title has "and
                        for sub_title in title.split("and"):
                            chapter_title.append(sub_title.strip())
                    elif "(" in title:  # case where title has brackets
                        for sub_title in title.split(" "):
                            if "(" in sub_title:
                                s = re.search(r"\((.*)\)", sub_title).group(1)
                                chapter_title.append(s.strip())
                            else:
                                if sub_title.strip() != "":
                                    chapter_title.append(sub_title.strip())
                    else:  # normal case
                        chapter_title.append(title.strip())
                i += 1
            elif i == 1 and chapter[i] == " ":  # case with no subtitles and no subchapters
                i += 1
            elif i == 1 and "General" in chapter[i]:  # case with no subtitles
                subchapters.append(chapter[i].strip())
                for j in range(i + 1, len(chapter)):
                    if chapter[j] != " ":
                        subchapters.append(chapter[j].strip())
                    else:
                        break
                break
            else:  # default case
                subtitles = chapter[i].strip().split(",")
                # for subtitle in subtitles:
                # chapter_subtitle.append(subtitle.strip())
                subchapters_index = 0
                if chapters_list.index(chapter) == 26:  # special case for chapter 26
                    chapter.remove(chapter[5])
                for j in range(i + 1, len(chapter)):
                    if chapter[j] != " ":  # until space between subtitle and subchapter
                        n1 = re.sub(r"\(", "", chapter[j].strip())
                        n2 = re.sub(r"\)", "", n1)
                        subtitles.append(n2.strip())
                    else:
                        subchapters_index = j
                        break
                subtitles_string = ",".join(subtitles)
                if re.findall(r"\(.*?\)", subtitles_string):
                    for s in re.findall(r"\(.*?\)", subtitles_string):
                        if "," in s:
                            variations = s.split(",")
                            for variation in variations:
                                n1 = re.sub(r"\(", "", variation)
                                n2 = re.sub(r"\)", "", n1)
                                if n2.strip().isalpha():
                                    chapter_subtitle.append(n2.strip())
                        else:
                            n1 = re.sub(r"\(", "", s)
                            n2 = re.sub(r"\)", "", n1)
                            chapter_subtitle.append(n2.strip())
                    if re.search(r"\(", subtitles_string):
                        cleanup = re.sub(r"\n", "", subtitles_string)
                        get_non_brackets = ",".join(re.findall(r"([^\(\)]+)(?:$|\()", cleanup))
                        seperated_subtitles = re.sub(r"\(", ",", get_non_brackets)
                        for s in seperated_subtitles.split(","):
                            if s.strip() != "":
                                n1 = re.sub(r"\(", "", s)
                                n2 = re.sub(r"\)", "", n1)
                                chapter_subtitle.append(n2.strip())
                else:
                    for s in subtitles_string.split(","):
                        if s.strip() != "":
                            n1 = re.sub(r"\(", "", s)
                            n2 = re.sub(r"\)", "", n1)
                            chapter_subtitle.append(n2.strip())
                if subchapters_index == len(chapter):  # case with no subchapters
                    break
                for j in range(subchapters_index + 1, len(chapter)):
                    if chapter[j] != " ":
                        if "(" in chapter[j].strip():  # case where subchapter has brackets
                            before_bracket = re.search(r"(.*)\(", chapter[j].strip()).group(1)
                            between_bracket = re.search(r"\((.*)\)", chapter[j].strip()).group(1)
                            if "," in between_bracket:
                                variations = between_bracket.split(",")
                                for variation in variations:
                                    if variation.strip().isalpha():
                                        subchapters.append(variation.strip())
                            else:
                                subchapters.append(between_bracket.strip())
                            subchapters.append(before_bracket.strip())
                        else:
                            subchapters.append(chapter[j].strip())
                    else:
                        break
                break

        parsed_chapter_titles.append({"chapterId": chapter_id.group(0), "titles": chapter_title})
        parsed_chapter_subtitles.append({"chapterId": chapter_id.group(0), "subtitles": chapter_subtitle})
        parsed_chapter_subchapters.append({"chapterId": chapter_id.group(0), "subchapters": subchapters})

    return parsed_chapter_titles, parsed_chapter_subtitles, parsed_chapter_subchapters


def subchapters_subchapters(subchapters):
    subsub = []
    id = 0
    chapters_subchapters = subchapters
    for chapter in chapters_subchapters:
        if chapter["chapterId"] == "18":  # special case for chapter 18
            subsub.append(
                {"id": id, "major": "Teeth", "minors": ["General", "incisors", "canine teeth", "molar teeth"]})
            id += 1
            subsub.append({"id": id, "major": "molar teeth", "minors": ["upper teeth", "lower teeth"]})
            id += 1
            for subchapter in chapter["subchapters"]:
                if "-" in subchapter:
                    chapter["subchapters"].remove(subchapter)
        elif chapter["chapterId"] == "27":  # special case for chapter 27

            subsub.append(
                {"id": id, "major": "Upper abdomen", "minors": ["Pancreas", "Liver", "Gall bladder", "Spleen"]})
            id += 1
            subsub.append({"id": id, "major": "Lower abdomen", "minors": ["Ileocecal region"]})
            id += 1
            subsub.append({"id": id, "major": "small pelvis", "minors": ["Ileocecal region"]})
            id += 1
            subsub.append({"id": id, "major": "Stool consistency", "minors": ["Diarrhea", "Constipation"]})
            id += 1
            subsub.append(
                {"id": id, "major": "Large intestine", "minors": ["Appendix", "vermiform appendix", "Colon", "Rectum"]})
            id += 1
            subsub.append(
                {"id": id, "major": "Flatulence", "minors": ["- General", "Flatulency", "Flatulence dislocation"]})
            id += 1
            break
        elif "-" in "".join(chapter["subchapters"]) and "one -sided" not in "".join(
                chapter["subchapters"]):  # special case where subchapter has hyphen
            i = 0
            while i < len(chapter["subchapters"]):
                if "-" not in chapter["subchapters"][i]:
                    i += 1
                else:
                    subsub.append({"id": id, "major": chapter["subchapters"][i - 1], "minors": []})
                    subsub[id]["minors"].append(
                        chapter["subchapters"][i].split("-")[1].strip())
                    for j in range(i + 1, len(chapter["subchapters"])):
                        if "-" not in chapter["subchapters"][j]:
                            id += 1
                            i = j
                            break
                        else:
                            subsub[id]["minors"].append(
                                chapter["subchapters"][j].split("-")[1].strip())
    return subsub, subchapters


def pipeline(input_file="./input/Chapters.pdf"):
    chapters_list = extract_chapters(input_file)
    chapters_titles, chapters_subtitles, chapters_subchapters = parse_chapters(chapters_list)
    sub_subchapters, chapters_subchapters = subchapters_subchapters(chapters_subchapters)

    for chapters_subchapter in chapters_subchapters:
        print(chapters_subchapter)

    with open("./output_json_files/chapters.json", "w") as outfile:
        json.dump(chapters_titles, outfile)

    with open("./output_json_files/subtitles.json", "w") as outfile:
        json.dump(chapters_subtitles, outfile)

    with open("./output_json_files/subchapters.json", "w") as outfile:
        json.dump(chapters_subchapters, outfile)

    with open("./output_json_files/sub_subchapters.json", "w") as outfile:
        json.dump(sub_subchapters, outfile)


if __name__ == "__main__":
    pipeline()
